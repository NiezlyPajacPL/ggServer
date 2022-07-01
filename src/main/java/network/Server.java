package network;

import helpers.Packet;

import java.net.DatagramPacket;

public interface Server extends Runnable {

    void sendPacket(Packet packetToSend);

    Packet receivePacket();
}
