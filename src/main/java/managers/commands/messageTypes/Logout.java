package managers.commands.messageTypes;

import managers.ConnectionData;

import java.net.InetAddress;

public class Logout extends MessageType{
    public String name;
    public ConnectionData connectionData;

    public Logout(String name,ConnectionData connectionData){
        this.name = name;
        this.connectionData = connectionData;
    }

}
