#!/bin/bash

# Set variables
CA_COMMON_NAME="My Kafka CA"
SERVER_COMMON_NAME="localhost" # Or the hostname clients use to connect
CLIENT_COMMON_NAME="Kafka Client"
KEYSTORE_PASSWORD="changeit" # Replace with your password
TRUSTSTORE_PASSWORD="changeit" # Replace with your password

# Create certs directory if it doesn't exist
mkdir -p certs

# 1. Generate CA Key and Certificate
echo "Generating CA certificate..."
openssl genrsa -out ca.key 2048
openssl req -x509 -new -nodes -key ca.key -sha256 -days 3650 -out ca.crt -subj "/CN=$CA_COMMON_NAME"

# 2. Generate Server Key and Certificate Signing Request (CSR)
echo "Generating server certificate..."
openssl genrsa -out server.key 2048
openssl req -new -key server.key -out server.csr -subj "/CN=$SERVER_COMMON_NAME"

# 3. Sign Server CSR with CA Certificate
echo "Signing server certificate..."
openssl x509 -req -in server.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out server.crt -days 365 -sha256

# 4. Generate Client Key and CSR
echo "Generating client certificate..."
openssl genrsa -out client.key 2048
openssl req -new -key client.key -out client.csr -subj "/CN=$CLIENT_COMMON_NAME"

# 5. Sign Client CSR with CA Certificate
echo "Signing client certificate..."
openssl x509 -req -in client.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out client.crt -days 365 -sha256

# 6. Create Server Keystore (JKS)
echo "Creating server keystore..."
openssl pkcs12 -export -in server.crt -inkey server.key -out server.p12 -name kafka -CAfile ca.crt -caname root -password pass:$KEYSTORE_PASSWORD
keytool -importkeystore -srckeystore server.p12 -srcstoretype PKCS12 -destkeystore kafka.server.keystore.jks -deststoretype JKS -deststorepass $KEYSTORE_PASSWORD -srcstorepass $KEYSTORE_PASSWORD -alias kafka -noprompt

# 7. Create Server Truststore (JKS)
echo "Creating server truststore..."
keytool -import -trustcacerts -keystore kafka.server.truststore.jks -storepass $TRUSTSTORE_PASSWORD -file ca.crt -alias root -noprompt

# 8. Create Client Keystore (JKS)
echo "Creating client keystore..."
openssl pkcs12 -export -in client.crt -inkey client.key -out client.p12 -name kafka -CAfile ca.crt -caname root -password pass:$KEYSTORE_PASSWORD
keytool -importkeystore -srckeystore client.p12 -srcstoretype PKCS12 -destkeystore kafka.client.keystore.jks -deststoretype JKS -deststorepass $KEYSTORE_PASSWORD -srcstorepass $KEYSTORE_PASSWORD -alias kafka -noprompt

# 9. Create Client Truststore (JKS)
echo "Creating client truststore..."
keytool -import -trustcacerts -keystore kafka.client.truststore.jks -storepass $TRUSTSTORE_PASSWORD -file ca.crt -alias root -noprompt

# Cleanup temporary files
rm server.csr server.p12 client.csr client.p12
