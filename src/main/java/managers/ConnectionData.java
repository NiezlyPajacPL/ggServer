package managers;

import java.net.InetAddress;

public class ConnectionData {
    private InetAddress inetAddress;
    private int port;

    public ConnectionData(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public InetAddress getInetAddress(){
        return inetAddress;
    }

    public int getPort(){
        return  port;
    }
}
