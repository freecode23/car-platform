package gpsProcessing;

public class Main {
    public static void main(String[] args) {
        // Docker run:
        // Read environment variable
        String bootstrapServers = System.getenv("KAFKA_BOOTSTRAP_SERVERS");
        if (bootstrapServers == null) {
            throw new IllegalStateException("KAFKA_BOOTSTRAP_SERVERS environment variable is not set");
        }

        String topicSubscribe = System.getenv("TOPIC_KAFKA_SENSOR");
        if (topicSubscribe == null) {
            throw new IllegalStateException("TOPIC_KAFKA_SENSOR environment variable is not set");
        }


        String consumerGroup = "gps-consumer-group-java";

        // Create and start Kafka consumer
        GpsProcessing gpsProcessing = new GpsProcessing(bootstrapServers, topicSubscribe, consumerGroup);
        gpsProcessing.startConsuming();
    }
}
