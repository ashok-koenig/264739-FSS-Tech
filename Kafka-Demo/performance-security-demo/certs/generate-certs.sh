#!/usr/bin/env bash
# Generates a throwaway, self-signed keystore/truststore pair for the performance-security-demo
# broker and its clients. Demo-only: one shared self-signed cert, static "changeit" password -
# see the README's production notes for what a real deployment needs instead.
#
# This folder is mounted into the broker container at /etc/kafka/secrets (see docker-compose.yml).
# The apache/kafka image's own /etc/kafka/docker/configure script requires SSL material to be
# handed over as *files* referenced by *_FILENAME/*_CREDENTIALS env vars, not raw
# KAFKA_SSL_KEYSTORE_LOCATION/PASSWORD values - the credentials files below hold the passwords
# it reads at container startup.
set -euo pipefail
cd "$(dirname "$0")"

PASSWORD=changeit

# idempotent: keytool refuses to overwrite an existing alias, so clear any previous run's output first
rm -f kafka.server.keystore.jks kafka.client.truststore.jks broker.cer \
  kafka_ssl_keystore_creds kafka_ssl_key_creds broker_jaas.conf

# the broker's own identity - used by the SSL and SASL_SSL listeners (see docker-compose.yml)
keytool -genkey -keystore kafka.server.keystore.jks -alias broker \
  -validity 365 -keyalg RSA -storepass "$PASSWORD" -keypass "$PASSWORD" \
  -dname "CN=localhost, OU=demo, O=demo, L=demo, S=demo, C=US"

# export the broker's public cert so clients can trust it
keytool -export -keystore kafka.server.keystore.jks -alias broker \
  -file broker.cer -storepass "$PASSWORD"

# client truststore - what order-service/notification-service/streams-service and acls/apply-acls.sh point at
keytool -import -keystore kafka.client.truststore.jks -alias broker \
  -file broker.cer -storepass "$PASSWORD" -noprompt

rm broker.cer

# credential files the broker's configure script reads its keystore/key passwords from
echo -n "$PASSWORD" > kafka_ssl_keystore_creds
echo -n "$PASSWORD" > kafka_ssl_key_creds

# static JAAS login-config file: unused in practice (this demo configures SASL/PLAIN per-listener
# via the KAFKA_LISTENER_NAME_CLIENT_PLAIN_SASL_JAAS_CONFIG property instead), but the configure
# script requires KAFKA_OPTS to point somewhere whenever a SASL_* protocol is present
touch broker_jaas.conf

echo "Generated kafka.server.keystore.jks, kafka.client.truststore.jks, kafka_ssl_keystore_creds, kafka_ssl_key_creds, broker_jaas.conf in $(pwd)"
