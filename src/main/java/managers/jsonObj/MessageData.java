package managers.jsonObj;

public class MessageData {
    private final Type type;
    private final String sender;
    private final String message;

    public MessageData(Type type, String sender, String message) {
        this.type = type;
        this.sender = sender;
        this.message = message;
    }
}
