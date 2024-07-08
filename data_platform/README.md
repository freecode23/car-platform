# Data Platform for Resque-Car

This directory contains the data platform microservices for the Resque-Car project. These services handle data processing, communication, and storage. Each service is containerized using Docker and can be managed using Docker Compose.

## Overview

The Resque-Car project is designed to test controlling a fleet of cars in disaster-stricken areas where Wi-Fi communication is impossible. The project features real-time GPS data processing, command communication, and data visualization. Each of the rectangular shapes in the architecture diagram represents a microservice running in a Docker container.

## Services

### Bridge

Handles the communication between Kafka and MQTT brokers. It consumes messages from Kafka and publishes them to MQTT, and vice versa.

### Command Sender

Receives commands via an API and sends them to Kafka.

### GPS Processing

Consumes GPS data from Kafka, processes it, and stores it in TimescaleDB.

### TimescaleDB

Stores the GPS data in a time-series database.

### Grafana

Used for visualizing the GPS data stored in TimescaleDB.

## 1. Run all containers using docker compose.
```
docker compose up --build -d
docker compose logs -f bridge
docker compose logs -f command-sender
docker compose logs -f gps-processing
```

Check if Kafka receives the message:
```
docker exec -it car_platform-kafka-1 /bin/sh
kafka-console-consumer --bootstrap-server localhost:9092 --topic topic-sensor --from-beginning
```

## 2. Query timescaledb using docker.
Connect to timescale db using postgreSQL:
```
docker run -it --rm --network host postgres psql -h localhost -U postgres -d gps
```

List all tables in the db:
\dt

Query the gps_data table:
SELECT * FROM gps_data;

## 3. Run Grafana to query the timescaleDB.
docker run -d -p 3005:3005 --name=grafana grafana/grafana
Host URL: timescaledb:5432
Database Name: gps
Username: postgres
Password: password
TLS/SSL Mode: disable

Access Grafana:
Open your browser and go to http://localhost:3000
Login with the default credentials (admin/admin)

## Make sure to create java keystore for AWS IoT connection.
https://github.com/aws/aws-iot-device-sdk-java

In the directory where we keep the certifcate enter:
```
openssl pkcs12 -export -in f276f24e1c2349d00ac57437d72620740582856ed359a4b64d1f17f9a88b7063-certificate.pem.crt -inkey f276f24e1c2349d00ac57437d72620740582856ed359a4b64d1f17f9a88b7063-private.pem.key -out p12_keystore -name alias
```

Use the password that we entered above here:
```
keytool -importkeystore -srckeystore p12_keystore -srcstoretype PKCS12 -srcstorepass <password> -alias alias -deststorepass <password> -destkeypass <password> -destkeystore my_keystore
```
