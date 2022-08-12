package managers.jsonObj;

public class LoginData {
    private final Type type;
    private final boolean isLoginSuccessful;

    public LoginData(Type type, boolean isLoginSuccessful) {
        this.type = type;
        this.isLoginSuccessful = isLoginSuccessful;
    }
}
