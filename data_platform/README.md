
## 1. Set up AWS and load certificates to SIM module
https://medium.com/@kellerhalssamuel/simcom7600-and-aws-iot-core-integration-guide-ceb7ff485289


# 2. Run Kafka Locally
Run zoo keeper:
bin/zookeeper-server-start.sh config/zookeeper.properties

Run kafka:
bin/kafka-server-start.sh config/server.properties

Create a kafka topic in kafka directory:
bin/kafka-topics.sh --create --topic topic-sensor --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

Use the Kafka Console Consumer to Read Messages from the Topic
bin/kafka-console-consumer.sh --topic topic-sensor --bootstrap-server localhost:9092 --from-beginning

# 3. Run just the Java bridge app:
## 3.1 Locally
Note that if you just run bridge app locally without running kafka,
it will not be able to pass message to kafka broker.
java -cp target/bridge-app-1.0-SNAPSHOT.jar:target/lib/\* bridge.Bridge

## 3.2 Docker 
Build and run the container:
docker build -t bridge-app .
docker run -it --rm bridge-app

Then run the app:
java -cp target/bridge-app-1.0-SNAPSHOT.jar:target/lib/\* bridge.Bridge


# 4. Run just the gps-processing app app:
Build and run the container:
docker compose up --build -t gps-processing-go

# 5. Run all (kafka, bridge, gps_processing, command-sender) using docker compose
docker compose up --build -d
docker compose logs -f bridge
docker compose logs -f command-sender
docker compose logs -f gps-processing

Check if Kafka receives the message:
docker exec -it car_platform-kafka-1 /bin/sh
kafka-console-consumer --bootstrap-server localhost:9092 --topic topic-sensor --from-beginning

To clean up docker:
docker system prune -a

# 5. To create java keystore
https://github.com/aws/aws-iot-device-sdk-java

In the directory where we keep the certifcate enter:
```
openssl pkcs12 -export -in f276f24e1c2349d00ac57437d72620740582856ed359a4b64d1f17f9a88b7063-certificate.pem.crt -inkey f276f24e1c2349d00ac57437d72620740582856ed359a4b64d1f17f9a88b7063-private.pem.key -out p12_keystore -name alias
```

Use the password that we entered above here:
```
keytool -importkeystore -srckeystore p12_keystore -srcstoretype PKCS12 -srcstorepass <password> -alias alias -deststorepass <password> -destkeypass <password> -destkeystore my_keystore
```


<!-- sudo rm -rf ~/go/pkg/mod -->

