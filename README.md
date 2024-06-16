
## 1. Set up AWS and load certificates to SIM module
https://medium.com/@kellerhalssamuel/simcom7600-and-aws-iot-core-integration-guide-ceb7ff485289


# 2. To run java program :
## Locally
java -cp target/mqtt-client-app-1.0-SNAPSHOT.jar:lib/\* carPlatform.MqttKafkaBridge

## Docker
docker build -t mqtt-client-app .
docker run -it --rm mqtt-client-app

## 4. To create java keystore
https://github.com/aws/aws-iot-device-sdk-java

In the directory where we keep the certifcate enter:
```
openssl pkcs12 -export -in f276f24e1c2349d00ac57437d72620740582856ed359a4b64d1f17f9a88b7063-certificate.pem.crt -inkey f276f24e1c2349d00ac57437d72620740582856ed359a4b64d1f17f9a88b7063-private.pem.key -out p12_keystore -name alias
```

Use the password that we entered above here:
```
keytool -importkeystore -srckeystore p12_keystore -srcstoretype PKCS12 -srcstorepass <password> -alias alias -deststorepass <password> -destkeypass <password> -destkeystore my_keystore
```