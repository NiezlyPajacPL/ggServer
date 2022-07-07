package managers.commands.messageTypes;

import helpers.SecuredPassword;
import managers.ConnectionData;

public class Registration extends MessageType {

    public String name;
    public String password;
//    public SecuredPassword securedPassword;
//    public ConnectionData connectionData;

    public Registration(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
