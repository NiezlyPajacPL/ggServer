package managers.commands.messageTypes;

import managers.ConnectionData;

import java.net.InetAddress;

public class Logout extends MessageType{
    public ConnectionData connectionData;

    public Logout(ConnectionData connectionData){
        this.connectionData = connectionData;
    }

}
