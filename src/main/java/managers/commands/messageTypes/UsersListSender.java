package managers.commands.messageTypes;

import managers.commands.messageTypes.MessageType;

import java.net.InetAddress;

public class UsersListSender extends MessageType {

    public InetAddress inetAddress;
    public int port;
    public byte[] message;

    public UsersListSender(InetAddress inetAddress, int port,byte[] message) {
        this.inetAddress = inetAddress;
        this.port = port;
        this.message = message;
    }

}
