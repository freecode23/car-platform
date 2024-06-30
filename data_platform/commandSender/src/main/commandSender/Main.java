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

        CommandSender commandSender = new CommandSender(bootstrapServers, kafkaTopic);
        
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
}