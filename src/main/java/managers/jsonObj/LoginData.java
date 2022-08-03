package managers.jsonObj;

public class LoginData {
    String type;
   // String message;
    boolean isLoginSuccessful;
    public LoginData(String type, boolean isLoginSuccessful){
        this.type = type;
        this.isLoginSuccessful = isLoginSuccessful;
     //   this.message =messageToSend;
    }
}
