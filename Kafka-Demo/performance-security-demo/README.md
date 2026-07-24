# Performance Tuning & Security

This folder is a self-contained copy of `order-service`, `notification-service`, and
`streams-service` (own `pom.xml`/`mvnw` each, unchanged Java code) with the performance and
security settings below actually applied to their `application.properties` files — not just
described. 

- **Performance tuning** — throughput vs. latency tradeoffs and message compression (Snappy, lz4).
- **Security** — encrypting traffic with SSL/TLS, authenticating clients with SASL, and
  authorizing them with ACLs.

Everything below still applies to the same `orders` / `payments` / `order-confirmations` topics
used elsewhere in this repo.

## Run it

1. **Generate demo certs** (self-signed, one-shot):

   ```bash
   bash certs/generate-certs.sh
   ```

   Produces `certs/kafka.server.keystore.jks` and `certs/kafka.client.truststore.jks` (the broker's
   identity and what clients trust it with), plus `certs/kafka_ssl_keystore_creds` and
   `certs/kafka_ssl_key_creds` (password files the broker container reads at startup) and an empty
   `certs/broker_jaas.conf` placeholder — see [How this went from a naive copy to a working
   setup](#how-this-went-from-a-naive-copy-to-a-working-setup) for why the broker needs files
   instead of literal passwords.

2. **Start the secured broker**:

   ```bash
   docker compose up -d
   ```

   `docker-compose.yml` runs a single-broker KRaft cluster (`perf-sec-broker`) with three
   listeners — `PLAINTEXT://9092` (kept for comparison), `SSL://9093`, and a `CLIENT` listener on
   `9094` speaking the `SASL_SSL` protocol (the one the copied services actually use) — plus
   `StandardAuthorizer` for ACLs. See the file's inline comments for why each block exists.

3. **Apply ACLs**:

   ```bash
   bash acls/apply-acls.sh
   ```

   Grants `order_service` producer + topic-creation access, `notification_service` consumer
   access, and `streams_service` its read/write/topic-creation/internal-topic access — see the
   script's comments for the exact operation each principal gets and why.

4. **Run each copied service** (three terminals, from this folder):

   ```bash
   cd order-service && ./mvnw spring-boot:run
   cd notification-service && ./mvnw spring-boot:run
   cd streams-service && ./mvnw spring-boot:run
   ```

   Each one's `application.properties` now points at `localhost:9094` (`SASL_SSL`) instead of the
   plaintext `9092` used by the original modules, with its own SASL identity and the client
   truststore from step 1.

5. **Exercise the pipeline** exactly like the other demos in this repo:

   ```bash
   curl -X POST localhost:8081/v1/orders -H "Content-Type: application/json" \
     -d '{"id":1,"item":"phone","quantity":2,"price":500.0}'

   curl -X POST localhost:8081/v1/payments -H "Content-Type: application/json" \
     -d '{"orderId":1,"amount":1000.0,"status":"SUCCESS"}'
   ```

   `notification-service` logs the order, `streams-service` logs `[STATEFUL-JOIN]` with a
   confirmation — the same behavior as the unsecured originals, now flowing entirely over
   SASL_SSL and gated by the ACLs from step 3. Verified end-to-end: `notification-service` logged
   `Order Received: Order{id=1, item='phone', quantity=2, price=500.0}` and `streams-service`
   logged `[STATEFUL-JOIN] OrderConfirmation{orderId=1, item='PHONE', orderAmount=1000.0,
   paidAmount=1000.0, paymentStatus='SUCCESS', confirmationMessage='CONFIRMED'}`.

6. **Prove the ACLs are actually doing something**: revoke `notification_service`'s read grant —

   ```bash
   docker exec perf-sec-broker /opt/kafka/bin/kafka-acls.sh --bootstrap-server localhost:9094 \
     --command-config /etc/kafka/secrets/admin.properties \
     --remove --allow-principal User:notification_service --operation READ --topic orders --force
   ```

   restart `notification-service`, and its log now shows `TopicAuthorizationException: Not
   authorized to access topics: [orders]` and the consumer container stops — instead of silently
   working. Restore the grant afterward by re-running `bash acls/apply-acls.sh`.

## Part 1: Performance Tuning

### Throughput vs. latency: it's a dial, not a switch

A Kafka producer buffers records per partition and sends them in batches. Three settings control
where you land on the throughput/latency line:

| Property | Effect |
|---|---|
| `linger.ms` | How long the producer waits for more records before sending a batch. `0` (default) sends almost immediately — lowest latency, smaller/more frequent batches. Raising it to `10`-`20` lets more records accumulate per batch, trading a few ms of latency for much higher throughput. |
| `batch.size` | Max bytes per partition batch. Bigger batches amortize per-request overhead (network round trips, broker-side bookkeeping) across more records — higher throughput, more memory used, more latency if traffic is bursty and batches take longer to fill. |
| `acks` | `0` = fire-and-forget (fastest, records can be lost). `1` = leader ack only (default, balanced). `all` = leader + all in-sync replicas ack (safest, slowest — needed alongside `retries`/`enable.idempotence=true` for exactly-once-ish guarantees). |

Applied in `order-service/src/main/resources/application.properties` (see its inline comments):

```properties
spring.kafka.producer.properties.linger.ms=20
spring.kafka.producer.properties.batch.size=32768
spring.kafka.producer.acks=all
```

### Compression: Snappy vs. lz4

`compression.type` compresses whole batches client-side before sending, so it trades producer CPU
for less network bandwidth and less broker disk — usually a clear win once messages aren't tiny.

- **snappy** — moderate compression ratio, very low CPU cost. Good default for JSON payloads like
  this repo's `Order`/`Payment` records where you want compression "for free" without slowing the
  producer down.
- **lz4** — similar CPU cost to snappy but a better compression ratio on most workloads; slightly
  more CPU than snappy on decompression. Generally the better pick unless you've measured
  otherwise — it's what's actually applied in `order-service`/`streams-service` here.
- (Not applied here, for reference) **gzip** compresses better than both but costs noticeably more
  CPU — better for cold/archival topics than a live order pipeline. **zstd** tunable, best ratio,
  costs the most CPU.

Applied — one property, no code change, no consumer-side config needed (the consumer decompresses
automatically based on a per-batch header):

```properties
# order-service/src/main/resources/application.properties
spring.kafka.producer.properties.compression.type=lz4

# streams-service/src/main/resources/application.properties - the topology's own producer (e.g. to order-confirmations)
spring.kafka.streams.properties.producer.compression.type=lz4
```

### Consumer-side tuning

Applied in `notification-service/src/main/resources/application.properties`:

```properties
spring.kafka.consumer.properties.fetch.min.bytes=1024
spring.kafka.consumer.properties.fetch.max.wait.ms=500
spring.kafka.consumer.max-poll-records=500
```

- `fetch.min.bytes` + `fetch.max.wait.ms` together mean "wait for at least 1KB of data, but don't
  wait longer than 500ms even if we don't have it" — fewer, fuller fetch requests instead of a
  fetch per record.
- `max-poll-records` caps how many records one `poll()` returns, bounding how long a single
  processing loop takes.

`streams-service` gets the analogous Streams-specific tuning instead —
`num.stream.threads` (parallelism across partitions) and `cache.max.bytes.buffering` (batches
KTable/state-store writes) — see its `application.properties` comments.

### Benchmarking compression's effect

Kafka ships perf-test scripts in `bin/` alongside `kafka-console-producer.sh`. Run them against
the `orders` topic on the plaintext listener (`9092`, still open in this demo's compose file) to
compare compressed vs. uncompressed throughput without needing SASL credentials:

```bash
# baseline: no compression
kafka-producer-perf-test.sh --topic orders --num-records 200000 --record-size 300 \
  --throughput -1 --producer-props bootstrap.servers=localhost:9092 acks=1

# with snappy
kafka-producer-perf-test.sh --topic orders --num-records 200000 --record-size 300 \
  --throughput -1 --producer-props bootstrap.servers=localhost:9092 acks=1 compression.type=snappy

# with lz4
kafka-producer-perf-test.sh --topic orders --num-records 200000 --record-size 300 \
  --throughput -1 --producer-props bootstrap.servers=localhost:9092 acks=1 compression.type=lz4
```

Each run prints records/sec and MB/sec — compare the MB/sec (network-bound) numbers across runs to
see compression's actual payoff for this record shape. Then on the consumer side:

```bash
kafka-consumer-perf-test.sh --topic orders --bootstrap-server localhost:9092 --messages 200000
```

## Part 2: Security

The broker everywhere else in this repo runs as plaintext (`PLAINTEXT://localhost:9092`) — fine for a local demo, but production clusters need encryption,
authentication, and authorization. `docker-compose.yml` here layers all three on, and
`order-service`/`notification-service`/`streams-service`'s copied `application.properties` files
actually connect through them. **These are demo/learning settings (self-signed certs, `PLAIN`
mechanism, static passwords) — not production-hardened; see the note at the end of each
subsection for what a real deployment needs instead.**

### SSL/TLS: encrypt traffic between clients and brokers

`certs/generate-certs.sh` creates the self-signed keystore/truststore and credential files;
`docker-compose.yml` wires them into the broker as files under `/etc/kafka/secrets` (mounted from
`./certs`) — the `apache/kafka:latest` image's own bootstrap script expects a keystore *file* plus
password *files*, not literal `KAFKA_SSL_KEYSTORE_LOCATION`/`PASSWORD` values:

```properties
KAFKA_SSL_KEYSTORE_FILENAME: kafka.server.keystore.jks
KAFKA_SSL_KEYSTORE_CREDENTIALS: kafka_ssl_keystore_creds
KAFKA_SSL_KEY_CREDENTIALS: kafka_ssl_key_creds
```

`KAFKA_SSL_CLIENT_AUTH` is intentionally left unset — this is one-way TLS (the broker proves its
identity to clients; client identity is handled by SASL below, not mutual TLS), so no broker-side
truststore is needed.

Each copied service's `application.properties` points at the client truststore:

```properties
spring.kafka.ssl.trust-store-location=file:../certs/kafka.client.truststore.jks
spring.kafka.ssl.trust-store-password=changeit
```

> **Production note:** use certificates from a real (or internal) CA, not self-signed ones; rotate
> them before expiry; and set `ssl.client.auth=required` on the broker for mutual TLS instead of
> only encrypting the transport.

### SASL: authenticate clients

The broker's `CLIENT` listener (port `9094`, `SASL_SSL` protocol) uses `PLAIN` — usernames/
passwords in a JAAS config (`docker-compose.yml`'s
`KAFKA_LISTENER_NAME_CLIENT_PLAIN_SASL_JAAS_CONFIG` — note the listener is named `CLIENT`, not
`SASL_SSL`; see the troubleshooting section below for why):

```properties
KAFKA_SASL_ENABLED_MECHANISMS: PLAIN
KAFKA_LISTENER_NAME_CLIENT_PLAIN_SASL_JAAS_CONFIG: >-
  org.apache.kafka.common.security.plain.PlainLoginModule required
  username="admin" password="admin-secret"
  user_admin="admin-secret"
  user_order_service="order-service-secret"
  user_notification_service="notification-service-secret"
  user_streams_service="streams-service-secret";
```

Client side, e.g. `order-service`'s producer (still connects with `security.protocol=SASL_SSL` —
the listener's internal name `CLIENT` is invisible to clients):

```properties
spring.kafka.bootstrap-servers=localhost:9094
spring.kafka.security.protocol=SASL_SSL
spring.kafka.properties.sasl.mechanism=PLAIN
spring.kafka.properties.sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username="order_service" password="order-service-secret";
```

`streams-service` needs this defined **twice**: once under the plain `spring.kafka.*` keys (used
by its `KafkaAdmin` bean, which auto-creates the `order-confirmations` topic) and once under the
unprefixed `spring.kafka.streams.properties.*` keys (used by the actual Kafka Streams runtime) —
these are two separate client stacks that don't share config. See its `application.properties`
comments.

> **Production note:** `PLAIN` ships credentials in a static JAAS config — fine for a demo, not for
> real secrets. Use `SCRAM-SHA-256`/`SCRAM-SHA-512` (credentials stored hashed, rotatable via
> `kafka-configs.sh --alter --add-config`) or `OAUTHBEARER` against a real identity provider
> instead.

### ACLs: authorize what each authenticated client can do

Authentication proves *who* a client is; ACLs decide *what* they're allowed to do.
`docker-compose.yml` turns on authorization:

```properties
KAFKA_AUTHORIZER_CLASS_NAME: org.apache.kafka.metadata.authorizer.StandardAuthorizer
KAFKA_SUPER_USERS: User:ANONYMOUS;User:admin
```

(`User:ANONYMOUS` is a super user too — see the troubleshooting section for why the KRaft
controller channel needs that in this single-node demo.)

`acls/apply-acls.sh` locks each principal down to only what it actually needs, mirroring the roles
already in this repo — `order-service` produces and owns its topics' creation,
`notification-service`/`streams-service` only ever consume (streams-service also owns
`order-confirmations`' creation and its own internal topics):

```bash
# order-service: producer AND owns KafkaTopicConfig's topic auto-creation - needs CREATE too, not just WRITE/DESCRIBE
kafka-acls.sh --bootstrap-server localhost:9094 --command-config admin.properties \
  --add --allow-principal User:order_service \
  --operation CREATE --operation WRITE --operation DESCRIBE --topic orders --topic payments

# notification-service: consumer, needs READ on its topic + its consumer group
kafka-acls.sh --bootstrap-server localhost:9094 --command-config admin.properties \
  --add --allow-principal User:notification_service \
  --operation READ --topic orders \
  --group notification-group

# streams-service: reads orders+payments, creates+writes order-confirmations, and needs
# access to its own internal changelog/repartition topics (prefixed by application-id)
kafka-acls.sh --bootstrap-server localhost:9094 --command-config admin.properties \
  --add --allow-principal User:streams_service \
  --operation READ --topic orders --topic payments \
  --operation CREATE --operation WRITE --topic order-confirmations

kafka-acls.sh --bootstrap-server localhost:9094 --command-config admin.properties \
  --add --allow-principal User:streams_service \
  --operation ALL --resource-pattern-type prefixed --topic streams-service-app- \
  --group streams-service-app
```

List what's configured at any point:

```bash
bash acls/apply-acls.sh   # re-running is safe, kafka-acls.sh --add is idempotent, and it ends with --list
```

> **Production note:** grant the narrowest operation set that works (`CREATE`+`WRITE`+`DESCRIBE`
> for producers that own their topics, `READ` for consumers — never `ALL` outside of the Streams
> app's own prefixed internal topics), and manage ACLs via code/CI rather than ad-hoc CLI runs so
> they're auditable.

## How this went from a naive copy to a working setup

The first pass at `docker-compose.yml`'s broker and layered on
the SSL/SASL/ACL properties everyone's tutorials show — and it failed in five different ways, one
after another, each only visible once the previous one was fixed. If you're adapting this pattern
elsewhere, these are the actual gotchas, in the order they were hit:

**1. `apache/kafka:latest` wants SSL material as *files*, not literal env vars.**
The first `docker-compose.yml` set `KAFKA_SSL_KEYSTORE_LOCATION`/`KAFKA_SSL_KEYSTORE_PASSWORD`
directly (the convention several other Kafka Docker images use). The broker crashed immediately
with:
```
Running in KRaft mode...
/etc/kafka/docker/configure: line 18: !1: unbound variable
SSL is enabled.
```
Reading `/etc/kafka/docker/configure` inside the image showed why: it calls `ensure
KAFKA_SSL_KEYSTORE_FILENAME` and derives the location from `/etc/kafka/secrets/$KAFKA_SSL_KEYSTORE_FILENAME`
— that env var was never set, so the `${!1}` indirect-parameter check inside its `ensure()` helper
fails under `set -u`. **Fix:** mount `./certs` at `/etc/kafka/secrets` (the path this script
expects) and set `KAFKA_SSL_KEYSTORE_FILENAME`/`KAFKA_SSL_KEYSTORE_CREDENTIALS`/
`KAFKA_SSL_KEY_CREDENTIALS` instead — `certs/generate-certs.sh` now also writes the
`kafka_ssl_keystore_creds`/`kafka_ssl_key_creds` password files this expects.

**2. That same script hard-requires `KAFKA_OPTS` once any `SASL_*` protocol is in play.**
Once the SSL fix above landed, the next failure was the same "unbound variable" pattern, this time
from `ensure KAFKA_OPTS` inside the script's SASL block, because `KAFKA_OPTS` was never set at
all. **Fix:** set `KAFKA_OPTS: -Djava.security.auth.login.config=/etc/kafka/secrets/broker_jaas.conf`.
The file it points to doesn't actually need real content (this demo's SASL config comes from the
per-listener JAAS property below, not the classic static login-config file) — it just has to
exist, so `certs/generate-certs.sh` `touch`es it.

**3. A listener literally named `SASL_SSL` breaks the env-var-to-property conversion.**
With the broker finally starting, SASL logins failed with:
```
java.lang.IllegalArgumentException: Could not find a 'KafkaServer' or 'sasl_ssl.KafkaServer' entry in the JAAS configuration.
```
The env var `KAFKA_LISTENER_NAME_SASL_SSL_PLAIN_SASL_JAAS_CONFIG` is supposed to map to the
property `listener.name.sasl_ssl.plain.sasl.jaas.config` — but the generic Kafka Docker convention
converts *every* underscore to a dot, so it actually became `listener.name.sasl.ssl.plain...`,
which matches nothing. This only bites listener *names* that themselves contain an underscore
(`SASL_SSL`, `SASL_PLAINTEXT`). **Fix:** renamed the listener from `SASL_SSL` to `CLIENT` (no
underscore) and mapped `CLIENT:SASL_SSL` in `KAFKA_LISTENER_SECURITY_PROTOCOL_MAP` — the env var
becomes `KAFKA_LISTENER_NAME_CLIENT_PLAIN_SASL_JAAS_CONFIG`, which converts unambiguously. Clients
are unaffected: they still connect with `security.protocol=SASL_SSL` on port 9094; the listener's
internal name is never visible to them.

**4. `StandardAuthorizer` also blocks KRaft's own internal controller traffic.**
Next failure, still at broker startup:
```
org.apache.kafka.common.errors.ClusterAuthorizationException: ... needs CLUSTER_ACTION permission.
ERROR [BrokerServer id=1] Fatal error during broker startup.
```
The single broker registers itself with its own controller over the (`PLAINTEXT`, unauthenticated)
`CONTROLLER` listener, as principal `User:ANONYMOUS` — and `StandardAuthorizer` applies to that
internal channel too once it's turned on, so the broker couldn't even talk to itself. **Fix:**
added `User:ANONYMOUS` to `KAFKA_SUPER_USERS` (semicolon-separated:
`User:ANONYMOUS;User:admin`). In a real deployment the controller listener runs on a private
network instead of granting `ANONYMOUS` broad access.

**5. The ACL grants were too narrow for the services that auto-create their own topics.**
With the broker healthy, `order-service` and `streams-service` both started fine but logged:
```
org.apache.kafka.common.errors.TopicAuthorizationException: Authorization failed.
```
Their `KafkaTopicConfig` classes create the `orders`/`payments`/`order-confirmations` topics via
their own `AdminClient` on startup — which needs `CREATE`, not just `WRITE`/`DESCRIBE`. **Fix:**
added `--operation CREATE` to `order_service`'s grant on `orders`/`payments` and to
`streams_service`'s grant on `order-confirmations` in `acls/apply-acls.sh`.

**6. `streams-service` has two separate client stacks with separate security config.**
Even with ACLs fixed, `streams-service` sat spinning forever with:
```
org.apache.kafka.common.errors.TimeoutException: Timed out waiting for a node assignment. Call: fetchMetadata
```
Its Kafka Streams runtime was correctly configured under `spring.kafka.streams.properties.*`, but
its separate `KafkaAdmin` bean (the one running `KafkaTopicConfig`, unrelated to Streams itself)
only reads the top-level `spring.kafka.*` keys — which had `bootstrap-servers` pointed at the
secured port but no `security.protocol`/SASL/SSL properties, so it kept trying (and failing) a
plaintext-style handshake against a `SASL_SSL` port. **Fix:** `streams-service`'s
`application.properties` now sets the full security block twice — once under `spring.kafka.*`
(for `KafkaAdmin`) and once under `spring.kafka.streams.properties.*` (for the Streams runtime).

**(Windows-only) Git Bash rewrites container paths before Docker sees them.** `docker exec
perf-sec-broker ... /etc/kafka/secrets/admin.properties` run from Git Bash gets its
`/etc/kafka/secrets/...` argument silently rewritten to a Windows path
(`C:/Program Files/Git/etc/...`), which the container obviously doesn't have. `acls/apply-acls.sh`
sets `export MSYS_NO_PATHCONV=1` at the top to disable that rewriting; it's a no-op on Linux/macOS.

All six fixes (plus the Windows one) are already applied in `docker-compose.yml`,
`certs/generate-certs.sh`, and `acls/apply-acls.sh` in this folder — this section exists so the
next person extending this pattern (a different listener setup, a different image, a different
OS) knows which assumptions to check first instead of re-discovering all of this from scratch.

## Cleanup

```bash
docker compose down
rm -f certs/kafka.server.keystore.jks certs/kafka.client.truststore.jks \
  certs/kafka_ssl_keystore_creds certs/kafka_ssl_key_creds certs/broker_jaas.conf certs/admin.properties
```

