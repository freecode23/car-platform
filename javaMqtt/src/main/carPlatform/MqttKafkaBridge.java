package carPlatform;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;

public class MqttKafkaBridge {

    // This should be the path from JVM dir, that is, the directory from which we invoke the java cmd.
    // This will be from javaMqtt dir.
    private static final String CERT_DIR = "./cert/";
    private static final String BROKER_END_POINT = "apv3187879vov-ats.iot.us-east-2.amazonaws.com";
    private static final String CLIENT_ID = "MacBook";
    private static final String PATH_TO_MY_KEYSTORE = CERT_DIR + "my_keystore";
    private static final String KEY_PASS = "dev123";
    

    public static void main(String[] args) throws Exception {
        MqttKafkaBridge bridge = new MqttKafkaBridge();
        bridge.start();
    }

    public void start() throws Exception {

        // 1. Connect to AWS IoT as client
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(PATH_TO_MY_KEYSTORE), KEY_PASS.toCharArray());
        AWSIotMqttClient client = new AWSIotMqttClient(BROKER_END_POINT, CLIENT_ID, keyStore, KEY_PASS);    
        client.connect();

        // 2. Publish command to AWS
        // TODO: This should come Rest API request instead of terminal command.
        // CommandPublisher pub = new CommandPublisher(client);
        // pub.startPublishing();

        // 3. Subscribe sensor topic from AWS
        SensorSubscriber sub = new SensorSubscriber(client);
        sub.startSubscribing();


        // 4. TODO: Forward the subscribed message to kafka
    }

}
