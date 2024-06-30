package bridge;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.amazonaws.services.iot.client.AWSIotMessage;



public class SensorMqttSub {

    private AWSIotMqttClient client;
    private static final String SENSOR_MQTT_TOPIC = "topic/sensor";

    public SensorMqttSub(AWSIotMqttClient client) {
        this.client = client;
    }

    public void startSubscribing() {
        SensorMqttTopic topic = new SensorMqttTopic(SENSOR_MQTT_TOPIC, AWSIotQos.QOS0);
        try {
            client.subscribe(topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
