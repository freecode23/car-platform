package carPlatform;


import java.io.FileInputStream;
import java.security.KeyStore;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MqttKafkaBridge {

    private static final String BROKER_END_POINT = "apv3187879vov-ats.iot.us-east-2.amazonaws.com";
    // This should be the path from JVM dir, that is, the directory from which we invoke the java cmd.
    // This will be from javaMqtt dir.
    private static final String CERT_DIR = "./cert/";
    private static final String PATH_TO_MY_KEYSTORE = CERT_DIR + "my_keystore";
    private static final String KEY_PASS = "dev123";

    private static final String CLIENT_ID = "MacBook";
    private static final String CMD_TOPIC = "topic/cmd";
    private static final String SENSOR_TOPIC = "topic/sensor";
    

    public static void main(String[] args) throws Exception {
        MqttKafkaBridge bridge = new MqttKafkaBridge();
        bridge.start();
    }

    public void start() throws Exception {
        System.out.println("hello yay!");
        System.out.println("Attempting to load keystore from: " + PATH_TO_MY_KEYSTORE);
        System.out.println("Absolute path: " + new java.io.File(PATH_TO_MY_KEYSTORE).getAbsolutePath());
        
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(PATH_TO_MY_KEYSTORE), KEY_PASS.toCharArray());
        AWSIotMqttClient client = new AWSIotMqttClient(BROKER_END_POINT, CLIENT_ID, keyStore, KEY_PASS);    
        client.connect();
        
    }


}
