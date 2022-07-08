package managers.commands.messageTypes;

import managers.ConnectionData;

public class Messenger extends MessageType {

    public String sender;
    public String receiver;
    public String message;

    public Messenger(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }

}
