package gpsProcessing;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;
import java.time.Duration;


public class GpsProcessing {
    public static void main(String[] args) {
        // Read environment variable
        String bootstrapServers = System.getenv("KAFKA_BOOTSTRAP_SERVERS");
        if (bootstrapServers == null) {
            throw new IllegalStateException("KAFKA_BOOTSTRAP_SERVERS environment variable is not set");
        }

        String topicSubscribe = System.getenv("TOPIC_KAFKA_SENSOR");
        if (topicSubscribe == null) {
            throw new IllegalStateException("TOPIC_KAFKA_SENSOR environment variable is not set");
        }

        // Create Kafka consumer that subscribe to a topic.
        String consumerGroup = "gps-consumer-group-java";
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Subscribe to sensor topic.
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(topicSubscribe));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("Consumed message: %s%n", record.value());
            }
        }
    }
}
