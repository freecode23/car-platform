package bridge;

import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.amazonaws.services.iot.client.AWSIotMessage;

import java.util.Properties;

// Kafka
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class SensorMqttTopic extends AWSIotTopic {

    private KafkaProducer<String, String> kafkaProducer;
    private String KAFKA_TOPIC;

    /**
     * Constructor will set up kafka producer that produce message for 
     * the message received on a topic.
     * @param topic
     * @param qos
     */
    public SensorMqttTopic(String topic, AWSIotQos qos) {
        super(topic, qos);
        this.KAFKA_TOPIC = topic.replace("/", "-");
        setupKafkaProducer();
    }


    private void setupKafkaProducer() {
        Properties props = new Properties();
        // Since the bridge-app is running as a Docker container within the same Docker Compose network, it should use the internal listener kafka:29092.
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProducer = new KafkaProducer<>(props);
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        String payload = message.getStringPayload();
        System.out.println("Received message: " + payload);

        // Forward to Kafka
        ProducerRecord<String, String> record = new ProducerRecord<>(KAFKA_TOPIC, payload);
        kafkaProducer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.printf("Sent record to topic %s partition %d offset %d%n",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }
}