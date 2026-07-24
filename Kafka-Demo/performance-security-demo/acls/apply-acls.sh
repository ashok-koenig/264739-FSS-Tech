#!/usr/bin/env bash
# Grants each demo service principal the least privilege it needs, via kafka-acls.sh running
# inside the broker container (bundled at /opt/kafka/bin in the apache/kafka image). Run this
# once after `docker compose up -d` and after certs/generate-certs.sh has produced the certs.
set -euo pipefail
cd "$(dirname "$0")"

# on Git Bash/MSYS (Windows), the shell rewrites absolute-looking paths like /etc/kafka/secrets/...
# into a Windows path before docker ever sees them; this opts out of that rewriting so the paths
# reach the container unchanged. No effect on Linux/macOS.
export MSYS_NO_PATHCONV=1

BROKER=perf-sec-broker
BOOTSTRAP=localhost:9094

# throwaway admin client config - authenticates as the broker's admin super user (see docker-compose.yml's
# KAFKA_SUPER_USERS) so it can grant ACLs to everyone else; written into ../certs, which is mounted
# into the container at /etc/kafka/secrets (same mount docker-compose.yml uses for the broker's own SSL material)
mkdir -p ../certs
cat > ../certs/admin.properties <<EOF
security.protocol=SASL_SSL
sasl.mechanism=PLAIN
sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username="admin" password="admin-secret";
ssl.truststore.location=/etc/kafka/secrets/kafka.client.truststore.jks
ssl.truststore.password=changeit
EOF

exec_acl() {
  docker exec "$BROKER" /opt/kafka/bin/kafka-acls.sh --bootstrap-server "$BOOTSTRAP" \
    --command-config /etc/kafka/secrets/admin.properties "$@"
}

# order-service: producer, and also owns KafkaTopicConfig's auto-creation of these topics on
# startup (via its own AdminClient) - needs CREATE in addition to WRITE (publish) and DESCRIBE
# (metadata lookups)
exec_acl --add --allow-principal User:order_service \
  --operation CREATE --operation WRITE --operation DESCRIBE --topic orders --topic payments

# notification-service: consumer only - needs READ on its source topic and on its own consumer group
exec_acl --add --allow-principal User:notification_service \
  --operation READ --topic orders \
  --group notification-group

# streams-service: reads orders+payments, writes order-confirmations (and owns that topic's
# auto-creation via its own KafkaTopicConfig, hence CREATE), and needs full control of its own
# internal changelog/repartition topics, which are all prefixed with its application-id (streams-service-app-)
exec_acl --add --allow-principal User:streams_service \
  --operation READ --topic orders --topic payments
exec_acl --add --allow-principal User:streams_service \
  --operation CREATE --operation WRITE --topic order-confirmations
exec_acl --add --allow-principal User:streams_service \
  --operation ALL --resource-pattern-type prefixed --topic streams-service-app- \
  --group streams-service-app

echo "--- current ACLs ---"
exec_acl --list
