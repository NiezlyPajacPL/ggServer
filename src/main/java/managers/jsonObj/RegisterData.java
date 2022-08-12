package managers.jsonObj;

public class RegisterData {
    private final Type type;
    private final boolean isRegistrationSuccessful;

    public RegisterData(Type type,boolean isRegistrationSuccessful){
        this.type = type;
        this.isRegistrationSuccessful = isRegistrationSuccessful;
    }
}
