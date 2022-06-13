package helpers;

import managers.ConnectionData;

public class PacketInformation {
    public byte[] bufToSend;
    public ConnectionData connectionData;

    public PacketInformation(byte[] bufToSend, ConnectionData connectionData) {
        this.bufToSend = bufToSend;
        this.connectionData = connectionData;
    }
}
