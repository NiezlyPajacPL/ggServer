package managers.jsonObj;

public class LoginData {
    Type type;
   // String message;
    boolean isLoginSuccessful;
    public LoginData(Type type, boolean isLoginSuccessful){
        this.type = type;
        this.isLoginSuccessful = isLoginSuccessful;
     //   this.message =messageToSend;
    }
}
