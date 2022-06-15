package network;

import helpers.Packet;
import managers.commandHandlers.CommandHandler;
import managers.SubtitlesPrinter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpServer implements Server {
    private DatagramSocket socket;

    SubtitlesPrinter subtitlesPrinter;


    public UdpServer(SubtitlesPrinter subtitlesPrinter,int port) throws SocketException {
        socket = new DatagramSocket(port);
        this.subtitlesPrinter = subtitlesPrinter;
    }

    public void run() {
       CommandHandler commandHandler = new CommandHandler(subtitlesPrinter);

        while (true) {
            DatagramPacket receivedPacket = receivePacket();

            Packet packetToSendInfo = commandHandler.commands(receivedPacket);
            sendPacket(packetToSendInfo);
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

