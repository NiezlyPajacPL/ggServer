package managers.jsonObj;

public class ServerMessageData {
    Type type;
    String message;

    public ServerMessageData(Type type, String message){
        this.type = type;
        this.message = message;
    }
}
