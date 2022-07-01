package helpers;

import managers.ConnectionData;

import java.net.InetAddress;

public class Packet {
    private byte[] data;
    private ConnectionData connectionData;

    public Packet(byte[] data, ConnectionData connectionData) {
        this.data = data;
        this.connectionData = connectionData;
    }

    public byte[] getData(){
        return data;
    }

    public ConnectionData getConnectionData(){
        return connectionData;
    }

    public InetAddress getAddress(){
        return connectionData.getInetAddress();
    }

    public int getPort(){
        return connectionData.getPort();
    }

}
