package network;

import helpers.DataBaseManager;
import helpers.MessageHelper;
import helpers.PasswordHasher;
import helpers.Packet;
import managers.ConnectionData;
import managers.SubtitlesPrinter;
import managers.commands.*;
import managers.commands.messageTypes.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UdpServer implements Server {
    private DatagramSocket socket;
    SubtitlesPrinter subtitlesPrinter;
    Map<String, ConnectionData> users = new HashMap<>();
    MessageHelper messageHelper = new MessageHelper(users);

    public UdpServer(int port,SubtitlesPrinter subtitlesPrinter) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.subtitlesPrinter = subtitlesPrinter;
    }

    PasswordHasher passwordHasher = new PasswordHasher();

    public void run() {
        while (true) {
            CommandMapperImpl commandMapper = new CommandMapperImpl(users);
            Commands commands = new Commands(subtitlesPrinter, messageHelper, users);
            MessageType messageType;
            Packet receivedPacket = receivePacket();
            messageType = commandMapper.mapCommand(receivedPacket);

            if (messageType instanceof Registration) {
                sendPacket(commands.registerUser((Registration) messageType));
            } else if (messageType instanceof UsersListSender) {
                sendPacket(commands.sendUsersList((UsersListSender) messageType));
            } else if (messageType instanceof Messenger) {
                sendPacket(commands.sendMessage((Messenger) messageType));
            }else if (messageType instanceof Login) {
                sendPacket(commands.loginUser((Login) messageType));
            } else if (messageType instanceof Logout) {
                sendPacket(commands.logoutUser((Logout) messageType));
            }
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
    public Packet receivePacket() {
        byte[] bufToReceive = new byte[256];
        DatagramPacket receivedDatagramPacket = new DatagramPacket(bufToReceive, bufToReceive.length);
        try {
            socket.receive(receivedDatagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Packet(receivedDatagramPacket.getData(),new ConnectionData(receivedDatagramPacket.getAddress(), receivedDatagramPacket.getPort()));
    }

}

