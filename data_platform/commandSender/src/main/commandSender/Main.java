package commandSender;

import commandSender.CommandSender;

public class Main {
    public static void main(String[] args) {

        // Get environment variables to set up Kafka producer
        String bootstrapServers = System.getenv("KAFKA_BOOTSTRAP_SERVERS");
        if (bootstrapServers == null) {
            throw new IllegalStateException("KAFKA_BOOTSTRAP_SERVERS environment variable is not set");
        }

        String kafkaTopic = System.getenv("TOPIC_KAFKA_CMD");
        if (kafkaTopic == null) {
            throw new IllegalStateException("TOPIC_KAFKA_CMD environment variable is not set");
        }

        // Local run:
        // String kafkaTopic = "topic/cmd";
        // String bootstrapServers = "kafka:29092";

        CommandSender commandSender = new CommandSender(bootstrapServers, kafkaTopic);
        
        while (true) {
            // Example: Send a "forward" command
            commandSender.sendCommand("w");
            
            // Pause for 10 s
            try {
                Thread.sleep(10000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            commandSender.sendCommand("s");
            
            // Pause for 10 s
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Close the producer
        // commandSender.close();
    }
}