
package bridge;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandPublisher {

    private AWSIotMqttClient client;
    private static final String CMD_TOPIC = "topic/cmd";

    public CommandPublisher(AWSIotMqttClient client) {
        this.client = client;
    }

    public void startPublishing() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String cmd;
            try {
                while (true) {
                    System.out.print("Enter command ('w' for forward, 's' for backward, 'q' to quit): ");
                    cmd = reader.readLine().trim();
                    if (cmd.equals("q")) {
                        System.out.println("Quit publishing");
                        break;
                    }
                    String command;
                    switch (cmd) {
                        case "w":
                            command = "forward";
                            break;
                        case "s":
                            command = "backward";
                            break;
                        case "a":
                            command = "left";
                            break;
                        case "d":
                            command = "right";
                            break;
                        case "x":
                            command = "stop";
                            break;
                        default:
                            System.out.println("Unknown command");
                            continue;
                    }

                    // Publish
                    String payload = createPayload(command);
                    CarMessage message = new CarMessage(CMD_TOPIC, AWSIotQos.QOS0, payload);
                    client.publish(message, 3000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private String createPayload(String command) {
        return String.format("{\"message\": \"%s\"}", command);
    }
}
