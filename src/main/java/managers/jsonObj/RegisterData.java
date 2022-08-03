package managers.jsonObj;

public class RegisterData {
    public Type type;
    private final boolean isRegistrationSuccessful;

    public RegisterData(Type type,boolean isRegistrationSuccessful){
        this.type = type;
        this.isRegistrationSuccessful = isRegistrationSuccessful;
    }

    public boolean isRegistrationSuccessful() {
        return isRegistrationSuccessful;
    }
}
