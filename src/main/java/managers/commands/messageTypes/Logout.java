package managers.commands.messageTypes;

import java.net.InetAddress;

public class Logout extends MessageType{
    public String name;
    public InetAddress inetAddress;
    public int port;

    public Logout(String name,InetAddress inetAddress, int port){
        this.name = name;
        this.inetAddress = inetAddress;
        this.port = port;
    }

}
