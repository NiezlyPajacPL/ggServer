package helpers;

import managers.ConnectionData;

import java.net.Socket;

public class Packet {
    private byte[] data;
    private Socket socket;

    public Packet(byte[] data, Socket socket){
        this.data = data;
        this.socket = socket;
    }

    public byte[] getData(){
        return data;
    }

    public Socket getSocket() {
        return socket;
    }
}
