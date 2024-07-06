package bridge;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * CommandPublisher class is a Kafka consumer that gets
 * cmd messages and publish them to AWS MQTT.
 */
public class CmdMqttPub {

    // Private fields
    private AWSIotMqttClient client;
    private static final String MQTT_CMD_TOPIC = "topic/cmd";

    private KafkaConsumer<String, String> consumerKafka;
    private String cmdMsgKafka;


    // Constructor
    public CmdMqttPub(AWSIotMqttClient client) {
        this.client = client;
        setupKafkaConsumer();
    }

    private void setupKafkaConsumer() {
        // Read the KAFKA_BOOTSTRAP_SERVERS environment variable
        String bootstrapServers = System.getenv("KAFKA_BOOTSTRAP_SERVERS");
        if (bootstrapServers == null) {
            throw new IllegalStateException("KAFKA_BOOTSTRAP_SERVERS environment variable is not set");
        }

        String topicKafkaCmd = System.getenv("TOPIC_KAFKA_CMD");
        if (topicKafkaCmd == null) {
            throw new IllegalStateException("TOPIC_KAFKA_CMD environment variable is not set");
        }

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "cmd-consumer-group");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerKafka = new KafkaConsumer<>(properties);
        consumerKafka.subscribe(Collections.singletonList(topicKafkaCmd));

    }


    public void publish() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {

            try {
                while (true) {
                    String cmdStrMqtt;
                    ConsumerRecords<String, String> records = consumerKafka.poll(100);
                    for (ConsumerRecord<String, String> record : records) {
                        cmdMsgKafka = record.value();
                        System.out.printf("Consumed Kafka message: %s%n", cmdMsgKafka);
                        switch (cmdMsgKafka) {
                            case "w":
                                cmdStrMqtt = "forward";
                                break;
                            case "s":
                                cmdStrMqtt = "backward";
                                break;
                            case "a":
                                cmdStrMqtt = "left";
                                break;
                            case "d":
                                cmdStrMqtt = "right";
                                break;
                            case "x":
                                cmdStrMqtt = "stop";
                                break;
                            default:
                                System.out.println("Unknown cmdStrMqtt");
                                continue;
                        }
    
                        // Publish
                        String payload = createPayload(cmdStrMqtt);
                        CmdMqttMsg cmdMsgMqtt = new CmdMqttMsg(MQTT_CMD_TOPIC, AWSIotQos.QOS0, payload);
                        client.publish(cmdMsgMqtt, 3000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                consumerKafka.close();
            }
        });
    }

    private String createPayload(String command) {
        return String.format("{\"message\": \"%s\"}", command);
    }
}
