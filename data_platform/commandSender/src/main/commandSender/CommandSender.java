package commandSender;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Properties;

@Service
public class CommandSender {

    private final String kafkaTopic;
    private final KafkaProducer<String, String> kafkaProducer;

    public CommandSender(@Value("${KAFKA_BOOTSTRAP_SERVERS:#{systemEnvironment['KAFKA_BOOTSTRAP_SERVERS']}}") String bootstrapServers,
                         @Value("${TOPIC_KAFKA_CMD:#{systemEnvironment['TOPIC_KAFKA_CMD']}}") String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        kafkaProducer = new KafkaProducer<>(props);
    }

    public void sendCommand(String command) {
        String payload = command;
        ProducerRecord<String, String> record = new ProducerRecord<>(this.kafkaTopic, payload);
        System.out.println("Sending command: " + payload);

        kafkaProducer.send(record, (RecordMetadata metadata, Exception exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.printf("Sent record to topic %s partition %d offset %d%n",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }

    @PreDestroy
    public void close() {
        kafkaProducer.close();
    }
}
