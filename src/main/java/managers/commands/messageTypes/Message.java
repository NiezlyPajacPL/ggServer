package managers.commands.messageTypes;

public class Message extends MessageType {

    public String sender;
    public String receiver;
    public String message;

    public Message(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }

}
