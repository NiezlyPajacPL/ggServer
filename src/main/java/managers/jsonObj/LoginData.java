package managers.jsonObj;

public class LoginData {
    Type type;
    String message;

    public LoginData(Type type, String messageToSend){
        this.type = type;
        this.message =messageToSend;
    }
}
