package managers.commands.messageTypes;

import managers.ConnectionData;

public class Login extends MessageType {

    public String name;
    public String password;
    public ConnectionData connectionData;

    public Login(String name,String password,ConnectionData connectionData){
        this.name = name;
        this.password = password;
        this.connectionData = connectionData;
    }

}
