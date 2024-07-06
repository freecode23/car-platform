package commandSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommandController {

    private final CommandSender commandSender;

    @Autowired
    public CommandController(CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @PostMapping("/sendCommand")
    public String sendCommand(@RequestParam String command) {
        commandSender.sendCommand(command);
        return "Command sent: " + command;
    }
}
