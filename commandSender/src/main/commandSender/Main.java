package commandSender;

public class Main {
    public static void main(String[] args) {
        CommandSender commandSender = new CommandSender();
        
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