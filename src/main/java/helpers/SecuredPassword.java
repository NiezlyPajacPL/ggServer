package helpers;

public class SecuredPassword {

    public String password;
    public String salt;

    public SecuredPassword(String password, String salt){
        this.password = password;
        this.salt = salt;
    }
}
