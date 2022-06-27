package managers.commands.messageTypes;

import java.net.InetAddress;

public class SomethingWentWrong extends MessageType{
    public byte[] message;
    public InetAddress inetAddress;
    public int port;

    public SomethingWentWrong(byte[] message, InetAddress inetAddress, int port){
        this.message = message;
        this.inetAddress = inetAddress;
        this.port = port;
    }
}
