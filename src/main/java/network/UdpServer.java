package network;

import helpers.Packet;
import helpers.StringToSendHelper;
import managers.ConnectionData;
import managers.SubtitlesPrinter;
import managers.refactor.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UdpServer implements Server {
    private DatagramSocket socket;
    SubtitlesPrinter subtitlesPrinter;
    Map<String, ConnectionData> clients = new HashMap<>();

    public UdpServer(SubtitlesPrinter subtitlesPrinter, int port) throws SocketException {
        socket = new DatagramSocket(port);
        this.subtitlesPrinter = subtitlesPrinter;
    }
    StringToSendHelper stringToSendHelper = new StringToSendHelper();
    public void run() {

        while (true) {
            DatagramPacket receivedPacket = receivePacket();

            CommandMapperImplementation commandMapperImplementation = new CommandMapperImplementation(clients);
            MessageType messageType;
            messageType = commandMapperImplementation.mapCommand(receivedPacket);

            if (messageType instanceof Registration) {

                registerUser((Registration) messageType);

            } else if (messageType instanceof UsersListSender) {

                sendUsersList((UsersListSender) messageType);

            } else if (messageType instanceof Messenger) {

                sendMessage((Messenger) messageType);

            }
            //   Packet packetToSend = commandUser.useCommand(receivedPacket);
            //   sendPacket(packetToSend);
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

    private void registerUser(Registration registration) {
        ConnectionData connectionData = new ConnectionData(registration.inetAddress, registration.port);
        String nickname = registration.name.replaceAll("[\\s\u0000]+", "").toLowerCase(Locale.ROOT);
        clients.put(nickname, connectionData);
        subtitlesPrinter.printLogClientRegistered(registration.name, connectionData.getInetAddress(), connectionData.getPort());
        byte[] bufToSend = stringToSendHelper.stringToSendHandler("Registered Successfully!", "", false);
        Packet packetToSend = new Packet(bufToSend, connectionData);
        sendPacket(packetToSend);
    }

    private void sendUsersList(UsersListSender usersListSender) {
        byte[] bufToSend = stringToSendHelper.stringToSendHandler(clients.toString(), "", false);
        subtitlesPrinter.printLogUsersListRequest();

        Packet packetToSend = new Packet(bufToSend, new ConnectionData(usersListSender.inetAddress, usersListSender.port));
        sendPacket(packetToSend);
    }

    private void sendMessage(Messenger messenger) {
        byte[] bufToSend = stringToSendHelper.stringToSendHandler(messenger.message, messenger.sender, true);
        subtitlesPrinter.printLogSuccessfullySentMessage(messenger.sender, messenger.receiver);

        Packet packetToSend = new Packet(bufToSend, new ConnectionData(messenger.destinationInetAddress, messenger.destinationPort));
        sendPacket(packetToSend);
    }
}

