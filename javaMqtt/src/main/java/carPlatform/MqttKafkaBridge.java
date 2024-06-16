package carPlatform;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.concurrent.CountDownLatch;

public class MqttKafkaBridge {

    private static final String BROKER_END_POINT = "ssl://apv3187879vov-ats.iot.us-east-2.amazonaws.com:8883";
    private static final String CLIENT_ID = "MacBook";
    private static final String CERT_NUM = "f276f24e1c2349d00ac57437d72620740582856ed359a4b64d1f17f9a88b7063";
    private static final String CMD_TOPIC = "topic/cmd";
    private static final String SENSOR_TOPIC = "topic/sensor";

    private static final String CERT_DIR = "../cert/";
    private static final String PATH_TO_ROOT = CERT_DIR + "AmazonRootCA1.pem";
    private static final String PATH_TO_KEY = CERT_DIR + CERT_NUM + "-private.pem.key";
    private static final String PATH_TO_CERT = CERT_DIR + CERT_NUM + "-certificate.pem.crt";
    

    public static void main(String[] args) throws Exception {
        System.out.println("hello memory persisitence");
        MemoryPersistence persistence = new MemoryPersistence();


    }


}
