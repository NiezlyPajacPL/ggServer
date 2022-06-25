package managers.commands.messageTypes;

import java.net.InetAddress;

public class Logout extends MessageType{
    public String name;
    public InetAddress inetAddress;
    public int port;
    public byte[] message;

    public Logout(String name,InetAddress inetAddress, int port, byte[] message){
        this.name = name;
        this.inetAddress = inetAddress;
        this.port = port;
        this.message = message;
    }

}
