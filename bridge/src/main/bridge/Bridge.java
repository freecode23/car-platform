package bridge;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.kafka.clients.producer.KafkaProducer;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;

public class Bridge {

    // This should be the path from JVM dir, that is, the directory from which we invoke the java cmd.
    // This will be from bridge dir.
    private static final String CERT_DIR = "./cert/";
    private static final String BROKER_END_POINT = "apv3187879vov-ats.iot.us-east-2.amazonaws.com";
    private static final String CLIENT_ID = "MacBook";
    private static final String PATH_TO_MY_KEYSTORE = CERT_DIR + "my_keystore";
    private static final String KEY_PASS = "dev123";
    

    public static void main(String[] args) throws Exception {
        Bridge bridge = new Bridge();
        bridge.start();
    }


    public void start() throws Exception {

        // 1. Connect to AWS IoT as client
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(PATH_TO_MY_KEYSTORE), KEY_PASS.toCharArray());
        AWSIotMqttClient client = new AWSIotMqttClient(BROKER_END_POINT, CLIENT_ID, keyStore, KEY_PASS);    
        client.connect();

        // 2. Command:
        // Kafka consumer and Mqtt publisher to AWS
        CmdMqttPub pub = new CmdMqttPub(client);
        pub.publish();

        // 3. Sensor:
        // Mqtt subscriber to AWS and Kafka producer
        // This will create a sensor topic that has a callback function.
        // When a msg is received on sensor topic, it will forward 
        // messages to kafka broker.
        SensorMqttSub sub = new SensorMqttSub(client);
        sub.startSubscribing();

    }



}
