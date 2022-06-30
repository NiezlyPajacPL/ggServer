package managers.commands.messageTypes;

import helpers.SecuredPassword;

import java.net.InetAddress;

public class Registration extends MessageType {

    public String name;
    public SecuredPassword securedPassword;
    public InetAddress inetAddress;
    public int port;

    public Registration(String name, SecuredPassword password, InetAddress inetAddress, int port) {
        this.name = name;
        this.securedPassword = password;
        this.inetAddress = inetAddress;
        this.port = port;
    }
}
