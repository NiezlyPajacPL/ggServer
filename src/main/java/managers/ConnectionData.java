package managers;

import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class ConnectionData {
    private InetAddress inetAddress;
    private int port;
    private InputStreamReader clientInputStream;

    public ConnectionData(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public ConnectionData(InputStreamReader clientInputStream){
        this.clientInputStream = clientInputStream;
    }

    public InputStreamReader getClientInputStream() {
        return clientInputStream;
    }

    public InetAddress getInetAddress(){
        return inetAddress;
    }

    public int getPort(){
        return  port;
    }
}
