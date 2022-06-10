package network;

import helpers.PacketInformation;
import managers.CommandHandler;
import managers.ConnectionData;
import managers.SubtitlesPrinter;
import managers.Temp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Server extends Thread {
    private DatagramSocket socket;
    private boolean running;
    private byte[] bufToReceive = new byte[256];
  //  private byte[] bufToSend;
    private ArrayList<String> clientList = new ArrayList<>();
    Map<String, ConnectionData> clients = new LinkedHashMap<>();
    List<Temp> list = new ArrayList<>();
    InetAddress inetAddress;
    int port;
    SubtitlesPrinter subtitlesPrinter;
  //  DatagramPacket receivedPacket;
 //   DatagramPacket packetToSend;
    CommandHandler commandHandler = new CommandHandler(socket);

    public Server(SubtitlesPrinter subtitlesPrinter) throws SocketException {
        socket = new DatagramSocket(4445);
        this.subtitlesPrinter = subtitlesPrinter;
    }

    public void run() {
        running = true;

        while (running) {
            try {
            DatagramPacket receivedPacket
                    = new DatagramPacket(bufToReceive, bufToReceive.length);
                DatagramPacket packetToSend;
                socket.receive(receivedPacket);
                PacketInformation packetToSendInfo = commandHandler.commands(receivedPacket);;
                packetToSend = new DatagramPacket(packetToSendInfo.bufToSend,
                        packetToSendInfo.bufToSend.length,
                        packetToSendInfo.connectionData.inetAddress,
                        packetToSendInfo.connectionData.port
                );
                socket.send(packetToSend);

            } catch (IOException e) {
                e.printStackTrace();
            }
            //
        }
        socket.close();
    }

}

