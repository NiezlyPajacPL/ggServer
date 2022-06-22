package helpers;

public class SecuredPassword {

    public String password;
    public String salt;

    public SecuredPassword(String pass, String salt){
        this.password = pass;
        this.salt = salt;
    }
}
