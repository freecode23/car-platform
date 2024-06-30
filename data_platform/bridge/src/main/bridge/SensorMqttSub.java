package bridge;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class SensorMqttSub {

    private AWSIotMqttClient client;
    private String topicMqttSensor;
    private KafkaProducer<String, String> kafkaProducer;

    public SensorMqttSub(AWSIotMqttClient client) {
        
        this.topicMqttSensor = System.getenv("TOPIC_MQTT_SENSOR");
        if (topicMqttSensor == null) {
            throw new IllegalStateException("TOPIC_MQTT_SENSOR environment variable is not set");
        }

        this.client = client;
        this.kafkaProducer = setupKafkaProducer();

    }

    private KafkaProducer<String, String> setupKafkaProducer() {
        // Read the KAFKA_BOOTSTRAP_SERVERS environment variable
        String bootstrapServers = System.getenv("KAFKA_BOOTSTRAP_SERVERS");
        if (bootstrapServers == null) {
            throw new IllegalStateException("KAFKA_BOOTSTRAP_SERVERS environment variable is not set");
        }

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<>(props);
    }

    public void startSubscribing() {
        SensorMqttTopic topic = new SensorMqttTopic(topicMqttSensor, AWSIotQos.QOS0, this.kafkaProducer);
        try {
            client.subscribe(topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
