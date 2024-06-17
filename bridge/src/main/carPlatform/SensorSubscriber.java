package carPlatform;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.amazonaws.services.iot.client.AWSIotMessage;



public class SensorSubscriber {

    private AWSIotMqttClient client;
    private static final String SENSOR_TOPIC = "topic/sensor";

    public SensorSubscriber(AWSIotMqttClient client) {
        this.client = client;
    }

    public void startSubscribing() {
        SensorTopic topic = new SensorTopic(SENSOR_TOPIC, AWSIotQos.QOS0);
        try {
            client.subscribe(topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
