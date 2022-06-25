package network;

import helpers.FileHandler;
import helpers.HashPassword;
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
    HashPassword hashPassword = new HashPassword();

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

            }else if(messageType instanceof Logout){

                logoutUser((Logout) messageType);

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
                fileHandler.overrideDataBase(registration.name + " " + registration.securedPassword.password + " " + registration.securedPassword.salt);
                clients.put(registration.name, connectionData);
                subtitlesPrinter.printLogGeneratedPassword();
                subtitlesPrinter.printLogClientRegistered(registration.name, connectionData.getInetAddress(), connectionData.getPort());
                Packet packetToSend = new Packet(registration.messageSuccessfullyRegistered, connectionData);
                sendPacket(packetToSend);

            } else {
                subtitlesPrinter.printLogClientFailedRegistration(registration.name, connectionData.getInetAddress(), connectionData.getPort());
                Packet packetToSend = new Packet(registration.messageFailedRegistration, connectionData);
                sendPacket(packetToSend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void sendUsersList(UsersListSender usersListSender) {
        subtitlesPrinter.printLogUsersListRequest();

        Packet packetToSend = new Packet(usersListSender.message, new ConnectionData(usersListSender.inetAddress, usersListSender.port));
        sendPacket(packetToSend);
    }

    private void sendMessage(Messenger messenger) {

        try {
            FileHandler fileHandler = new FileHandler();
            if(fileHandler.clientExistInDataBase(messenger.receiver)){
            byte[] bufToSend = stringToSendHelper(messenger.message, messenger.sender);
            subtitlesPrinter.printLogSuccessfullySentMessage(messenger.sender, messenger.receiver, messenger.message);
            Packet packetToSend = new Packet(bufToSend, new ConnectionData(messenger.destinationInetAddress, messenger.destinationPort));
            sendPacket(packetToSend);
            }else{
                subtitlesPrinter.printLogMessageNotSent(messenger.sender, messenger.receiver);
                Packet packetToSend = new Packet(messenger.failedToSendMessage, new ConnectionData(messenger.destinationInetAddress, messenger.destinationPort));
                sendPacket(packetToSend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginUser(Login login) {
        ConnectionData connectionData = new ConnectionData(login.inetAddress, login.port);
        try {
            FileHandler fileHandler = new FileHandler();
            if(fileHandler.clientExistInDataBase(login.name)) {
                subtitlesPrinter.printLogClientFoundInDB(login.name);
                if (hashPassword.checkIfPasswordMatches(login.name, login.password)) {
                    subtitlesPrinter.printLogClientLoggedIn(login.name);
                    clients.put(login.name, connectionData);
                    Packet packetToSend = new Packet(login.messageSuccessfullyLogged, connectionData);
                    sendPacket(packetToSend);
                } else {
                    Packet packetToSend = new Packet(login.messageFailedLogin, connectionData);
                    sendPacket(packetToSend);
                    subtitlesPrinter.printLogClientDoesNotExist(login.name);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logoutUser(Logout logout){
        subtitlesPrinter.printLogClientLoggedOut(logout.name);
        clients.remove(logout.name);
        Packet packetToSend = new Packet(logout.message, new ConnectionData(logout.inetAddress, logout.port));
        sendPacket(packetToSend);
    }

    private byte[] stringToSendHelper(String text, String sender) {
        String textToSend = sender + ": " + text;
        return textToSend.getBytes(StandardCharsets.UTF_8);
    }
}

