package managers;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;

public class ConnectionData {
    private InetAddress inetAddress;
    private int port;
    private InputStreamReader receivingStream;
    private OutputStream sendingStream;

    public ConnectionData(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public ConnectionData(InputStreamReader clientInputStream,OutputStream clientSendingStream){
        this.receivingStream = clientInputStream;
        this.sendingStream = clientSendingStream;
    }

    public InputStreamReader getReceivingStream() {
        return receivingStream;
    }

    public OutputStream getSendingStream() {
        return sendingStream;
    }

    public InetAddress getInetAddress(){
        return inetAddress;
    }

    public int getPort(){
        return  port;
    }
}
