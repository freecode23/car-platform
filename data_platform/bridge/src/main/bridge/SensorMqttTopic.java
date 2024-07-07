package bridge;

import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.amazonaws.services.iot.client.AWSIotMessage;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class SensorMqttTopic extends AWSIotTopic {

    private KafkaProducer<String, String> kafkaProducer;
    private String KAFKA_TOPIC;

    /**
     * Constructor will set up kafka producer that produce message for 
     * the message received on a topic.
     * @param topic
     * @param qos
     */
    public SensorMqttTopic(String topic, AWSIotQos qos, KafkaProducer<String, String> kafkaProducer) {
        super(topic, qos);
        this.KAFKA_TOPIC = topic.replace("/", "-");
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        String payload = message.getStringPayload();

        // Forward to Kafka
        ProducerRecord<String, String> record = new ProducerRecord<>(KAFKA_TOPIC, payload);
        kafkaProducer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } 
            // else {
            //     System.out.printf("Sent record to topic %s partition %d offset %d%n",
            //             metadata.topic(), metadata.partition(), metadata.offset());
            // }
        });
    }
}
