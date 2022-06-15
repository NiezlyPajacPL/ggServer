package helpers;

import managers.ConnectionData;

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

}
