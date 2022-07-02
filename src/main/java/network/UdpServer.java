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
    DataBaseManager dataBaseManager;
    {
        try {
            dataBaseManager = new DataBaseManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UdpServer(SubtitlesPrinter subtitlesPrinter, int port) throws SocketException {
        socket = new DatagramSocket(port);
        this.subtitlesPrinter = subtitlesPrinter;
    }

    PasswordHasher passwordHasher = new PasswordHasher();

    public void run() {
        while (true) {
            CommandMapperImpl commandMapper = new CommandMapperImpl(users);
            MessageType messageType;
            Packet receivedPacket = receivePacket();
            messageType = commandMapper.mapCommand(receivedPacket);

            if (messageType instanceof Registration) {
                registerUser((Registration) messageType);
            } else if (messageType instanceof UsersListSender) {
                sendUsersList((UsersListSender) messageType);
            } else if (messageType instanceof Messenger) {
                sendMessage((Messenger) messageType);
            } else if (messageType instanceof Login) {
                loginUser((Login) messageType);
            } else if (messageType instanceof Logout) {
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

    private void registerUser(Registration registration) {
        ConnectionData connectionData = new ConnectionData(registration.inetAddress, registration.port);
        if (!dataBaseManager.clientExistInDB(registration.name) && registration.name != null) {
            dataBaseManager.saveClient(registration.name, registration.securedPassword);
            users.put(registration.name, connectionData);
            subtitlesPrinter.printLogGeneratedPassword();
            subtitlesPrinter.printLogClientRegistered(registration.name, connectionData.getInetAddress(), connectionData.getPort());
            Packet packetToSend = new Packet(messageHelper.registeredSuccessfully.getBytes(StandardCharsets.UTF_8), connectionData);
            sendPacket(packetToSend);

        } else {
            if (registration.name == null) {
                subtitlesPrinter.printLogClientRegistrationFailedCommand(registration.inetAddress, registration.port);
            } else {
                subtitlesPrinter.printLogClientFailedRegistration(registration.name, connectionData.getInetAddress(), connectionData.getPort());
            }
            Packet packetToSend = new Packet(messageHelper.nicknameAlreadyTaken.getBytes(StandardCharsets.UTF_8), connectionData);
            sendPacket(packetToSend);
        }

    }

    private void sendUsersList(UsersListSender usersListSender) {
        subtitlesPrinter.printLogUsersListRequest();

        Packet packetToSend = new Packet(messageHelper.clientList().getBytes(StandardCharsets.UTF_8), new ConnectionData(usersListSender.inetAddress, usersListSender.port));
        sendPacket(packetToSend);
    }

    private void sendMessage(Messenger messenger) {

        if (dataBaseManager.clientExistInDB(messenger.receiver)) {
            byte[] messageToSend = stringToSendHelper(messenger.message, messenger.sender);
            subtitlesPrinter.printLogSuccessfullySentMessage(messenger.sender, messenger.receiver, messenger.message);
            Packet packetToSend = new Packet(messageToSend, new ConnectionData(messenger.destinationInetAddress, messenger.destinationPort));
            sendPacket(packetToSend);
        } else {
            subtitlesPrinter.printLogMessageNotSent(messenger.sender, messenger.receiver);
            sendPacket(new Packet(messageHelper.failedToSendMessage.getBytes(StandardCharsets.UTF_8), new ConnectionData(messenger.destinationInetAddress, messenger.destinationPort)));
        }
    }

    private void loginUser(Login login) {
        ConnectionData connectionData = new ConnectionData(login.inetAddress, login.port);
        if (dataBaseManager.clientExistInDB(login.name)) {
            subtitlesPrinter.printLogClientFoundInDB(login.name);
            if (passwordHasher.checkIfPasswordMatches(login.name, login.password) && login.name != null) {
                subtitlesPrinter.printLogClientLoggedIn(login.name);
                users.put(login.name, connectionData);
                Packet packetToSend = new Packet(messageHelper.successfullyLoggedIn(login.name).getBytes(StandardCharsets.UTF_8), connectionData);
                sendPacket(packetToSend);
            } else {
                sendPacket(new Packet(messageHelper.failedLogin.getBytes(StandardCharsets.UTF_8), connectionData));
            }
        } else {
            sendPacket(new Packet(messageHelper.failedLogin.getBytes(StandardCharsets.UTF_8), connectionData));
            subtitlesPrinter.printLogClientDoesNotExist(login.name);
        }
    }

    private void logoutUser(Logout logout) {
        subtitlesPrinter.printLogClientLoggedOut(logout.name);
        users.remove(logout.name);
        Packet packetToSend = new Packet(messageHelper.loggedOut.getBytes(StandardCharsets.UTF_8),new ConnectionData(logout.inetAddress, logout.port));
        sendPacket(packetToSend);
    }

    private byte[] stringToSendHelper(String text, String sender) {
        String textToSend = sender + ": " + text;
        return textToSend.getBytes(StandardCharsets.UTF_8);
    }
}

