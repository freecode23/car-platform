package commandSender;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class CommandSender {


    public static void main(String[] args) {
        CommandSender commandSender = new CommandSender("kafka:29092");
        
        while (true) {

            // Example: Send a "forward" command
            commandSender.sendCommand("w");
            
            // Example: Send a "backward" command
            commandSender.sendCommand("s");
            // Pause for 3 seconds
            try {
                Thread.sleep(3000); // 3000 milliseconds = 3 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Close the producer
        // commandSender.close();
    }

    private static final String KAFKA_TOPIC = "topic-cmd";
    private KafkaProducer<String, String> kafkaProducer;

    public CommandSender(String bootstrapServers) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        kafkaProducer = new KafkaProducer<>(props);
    }

    public void sendCommand(String command) {
        // String payload = String.format("{\"message\": \"%s\"}", command);
        String payload = command;
        ProducerRecord<String, String> record = new ProducerRecord<>(KAFKA_TOPIC, payload);
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

    public void close() {
        kafkaProducer.close();
    }
}
