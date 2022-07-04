package managers.commands.messageTypes;

import helpers.SecuredPassword;
import managers.ConnectionData;

public class Registration extends MessageType {

    public String name;
    public SecuredPassword securedPassword;
    public ConnectionData connectionData;
//    public InetAddress inetAddress;
//    public int port;

    public Registration(String name, SecuredPassword password,ConnectionData connectionData) {
        this.name = name;
        this.securedPassword = password;
        this.connectionData = connectionData;
    }
}
