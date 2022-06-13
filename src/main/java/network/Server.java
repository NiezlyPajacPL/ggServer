package network;

import helpers.PacketInformation;
import managers.CommandHandler;
import managers.ConnectionData;
import managers.SubtitlesPrinter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;

public class Server extends Thread implements PacketHandler{
    private DatagramSocket socket;
    private boolean running;
    //  private byte[] bufToSend;
    SubtitlesPrinter subtitlesPrinter;


    public Server(SubtitlesPrinter subtitlesPrinter) throws SocketException {
        socket = new DatagramSocket(4445);
        this.subtitlesPrinter = subtitlesPrinter;
    }

    public void run() {
        running = true;
        CommandHandler commandHandler = null;
        commandHandler = new CommandHandler(subtitlesPrinter);

        while (running) {
            DatagramPacket receivedPacket = receivePacket();

            PacketInformation packetToSendInfo = commandHandler.commands(receivedPacket);
            sendPacket(packetToSendInfo);
        }
        socket.close();
    }

    @Override
    public void sendPacket(PacketInformation packetToSendInformation) {
      DatagramPacket packetToSend = new DatagramPacket(packetToSendInformation.bufToSend,
                packetToSendInformation.bufToSend.length,
                packetToSendInformation.connectionData.inetAddress,
                packetToSendInformation.connectionData.port
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
        DatagramPacket receivedPacket
                = new DatagramPacket(bufToReceive, bufToReceive.length);

        try {
            socket.receive(receivedPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receivedPacket;
    }
}

