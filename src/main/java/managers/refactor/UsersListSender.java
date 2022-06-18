package managers.refactor;

import java.net.InetAddress;

public class UsersListSender extends MessageType {

    public InetAddress inetAddress;
    public int port;

    UsersListSender(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

}
