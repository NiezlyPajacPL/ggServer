package helpers;

import java.net.Socket;

public class Packet {
    private final byte[] data;
    private final Socket socket;

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
