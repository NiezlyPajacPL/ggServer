package managers;

import java.net.InetAddress;

public class ConnectionData {
    public InetAddress inetAddress;
    public int port;

    public ConnectionData(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }
}
