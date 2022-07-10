package managers.commands.messageTypes;

public class Login extends MessageType {

    public String name;
    public String password;

    public Login(String name,String password){
        this.name = name;
        this.password = password;
    }

}
