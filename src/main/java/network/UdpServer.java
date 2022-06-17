package network;

import helpers.Packet;
import managers.ConnectionData;
import managers.commandHandlers.CommandHandler;
import managers.SubtitlesPrinter;
import managers.commandHandlers.CommandUser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class UdpServer implements Server {
    private DatagramSocket socket;
    SubtitlesPrinter subtitlesPrinter;
    Map<String, ConnectionData> clients = new HashMap<>();

    public UdpServer(SubtitlesPrinter subtitlesPrinter,int port) throws SocketException {
        socket = new DatagramSocket(port);
        this.subtitlesPrinter = subtitlesPrinter;
    }

    public void run() {
        CommandUser commandUser = new CommandUser(clients,subtitlesPrinter);
        while (true) {
            DatagramPacket receivedPacket = receivePacket();
            Packet packetToSend = commandUser.useCommand(receivedPacket);
            sendPacket(packetToSend);
        }
    }

    @Override
    public void sendPacket(Packet packetToSendInformation) {
        DatagramPacket packetToSend = new DatagramPacket(packetToSendInformation.getData(),
                packetToSendInformation.getData().length,
                packetToSendInformation.getConnectionData().getInetAddress(),
                packetToSendInformation.getConnectionData().getPort()
        );
        try {
            socket.send(packetToSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DatagramPacket receivePacket() {
        byte[] bufToReceive = new byte[256];
        DatagramPacket receivedPacket = new DatagramPacket(bufToReceive, bufToReceive.length);
        try {
            socket.receive(receivedPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receivedPacket;
    }
}

