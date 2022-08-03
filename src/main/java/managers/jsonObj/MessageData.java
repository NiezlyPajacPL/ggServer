package managers.jsonObj;

public class MessageData {
    public Type type;
    private String sender;
    private String message;

    public MessageData(Type type, String sender, String message) {
        this.type = type;
        this.sender = sender;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
