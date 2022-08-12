package helpers;

public class ClientLoginInfo {
    private final String nickname;
    private final SecuredPassword securedPassword;

    public ClientLoginInfo(String nickname, SecuredPassword securedPassword){
        this.nickname = nickname;
        this.securedPassword = securedPassword;
    }

    public String getNickname() {
        return nickname;
    }

    public SecuredPassword getSecuredPassword() {
        return securedPassword;
    }

    public String savePassword(){
        return securedPassword.getPassword();
    }
}
