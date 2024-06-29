package command;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;


public class Command {

    public static void main(String[] args) {


        // Set up Kafka producer
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        String KAFKA_TOPIC = "topic-cmd";
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
        

        // Send command if we receive an API request.
        // String payload = message.getStringPayload();
        String payload = "{\"message\": \"forward\"}";
        ProducerRecord<String, String> record = new ProducerRecord<>(KAFKA_TOPIC, payload);
        System.out.println("Received message: " + payload);
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
