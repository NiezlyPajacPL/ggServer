package network;

import helpers.FileHandler;
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
    Map<String, ConnectionData> clients = new HashMap<>();

    public UdpServer(SubtitlesPrinter subtitlesPrinter, int port) throws SocketException {
        socket = new DatagramSocket(port);
        this.subtitlesPrinter = subtitlesPrinter;
    }

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

            } else if (messageType instanceof Login) {

                loginUser((Login) messageType);

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
        try {
            FileHandler fileHandler = new FileHandler();
            if (!fileHandler.clientExistInDataBase(registration.name)) {
                fileHandler.overrideDataBase(registration.name + " " + registration.password);
                clients.put(registration.name, connectionData);
                subtitlesPrinter.printLogClientRegistered(registration.name, connectionData.getInetAddress(), connectionData.getPort());
                byte[] bufToSend = stringToSendHelper("Registered Successfully!", "", false);
                Packet packetToSend = new Packet(bufToSend, connectionData);
                sendPacket(packetToSend);
            }else{
                byte[] bufToSend = stringToSendHelper("Nickname is already taken. :(", "", false);
                Packet packetToSend = new Packet(bufToSend, connectionData);
                sendPacket(packetToSend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void sendUsersList(UsersListSender usersListSender) {
        byte[] bufToSend = stringToSendHelper(clients.toString(), "", false);
        subtitlesPrinter.printLogUsersListRequest();

        Packet packetToSend = new Packet(bufToSend, new ConnectionData(usersListSender.inetAddress, usersListSender.port));
        sendPacket(packetToSend);
    }

    private void sendMessage(Messenger messenger) {
        byte[] bufToSend = stringToSendHelper(messenger.message, messenger.sender, true);
        subtitlesPrinter.printLogSuccessfullySentMessage(messenger.sender, messenger.receiver);

        Packet packetToSend = new Packet(bufToSend, new ConnectionData(messenger.destinationInetAddress, messenger.destinationPort));
        sendPacket(packetToSend);
    }

    public void loginUser(Login login) {
        ConnectionData connectionData = new ConnectionData(login.inetAddress, login.port);
        try {
            FileHandler fileHandler = new FileHandler();
            if (fileHandler.doesInputMatchDataBase(login.name + " " + login.password)) {
                clients.put(login.name, connectionData);
                byte[] bufToSend = stringToSendHelper("Hello again " + login.name + "!", "", false);
                Packet packetToSend = new Packet(bufToSend, connectionData);
                sendPacket(packetToSend);
            } else {
                System.out.println(login.name + " does NOT exist in DB");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public byte[] stringToSendHelper(String text, String sender, boolean senderPrinted) {
        if (senderPrinted) {
            String textToSend = sender + ": " + text;
            return textToSend.getBytes(StandardCharsets.UTF_8);
        }
        return text.getBytes(StandardCharsets.UTF_8);
    }
}

