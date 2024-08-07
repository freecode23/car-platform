package bridge;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;

public class CmdMqttMsg extends AWSIotMessage {
    
    /**
     * Constructor
     * @param topic
     * @param qos
     * @param payload
     */
    public CmdMqttMsg(String topic, AWSIotQos qos, String payload) {
        super(topic, qos, payload);
    }

    @Override
    public void onSuccess() {
        System.out.println("Message publishing succeeded");
    }

    @Override
    public void onFailure() {
        System.out.println("Message publishing failed");
    }

    @Override
    public void onTimeout() {
        System.out.println("Message publishing timed out");
    }
}
    