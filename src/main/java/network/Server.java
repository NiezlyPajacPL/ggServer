package network;

import helpers.Packet;

public interface Server extends Runnable {

    void sendPacket(Packet packetToSend);

    Packet receivePacket();
}
