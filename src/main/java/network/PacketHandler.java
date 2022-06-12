package network;

import helpers.PacketInformation;

import java.net.DatagramPacket;

public interface PacketHandler {

    void sendPacket(PacketInformation packetToSend);

    DatagramPacket receivePacket();
}
