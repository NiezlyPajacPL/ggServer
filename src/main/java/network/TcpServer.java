package network;

import helpers.DataBaseManager;
import helpers.MessageHelper;
import helpers.Packet;
import helpers.PasswordHasher;
import managers.ConnectionData;
import managers.SubtitlesPrinter;
import managers.commands.CommandMapperImpl;
import managers.commands.messageTypes.*;

import java.io.*;
import java.net.DatagramPacket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TcpServer implements Server {
    protected Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;
    SubtitlesPrinter subtitlesPrinter;
    Map<String, ConnectionData> users = new HashMap<>();
    MessageHelper messageHelper = new MessageHelper(users);
    PasswordHasher passwordHasher = new PasswordHasher();

    public TcpServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    public void sendPacket(Packet packetToSend) {

    }

    @Override
    public Packet receivePacket() {
        String message = "UNKNOWN";
        try {
            message = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Packet(message.getBytes(StandardCharsets.UTF_8), new ConnectionData(clientSocket.getInetAddress(), clientSocket.getPort()));
    }

    private void registerUser(Registration registration) {
        ConnectionData connectionData = new ConnectionData(registration.inetAddress, registration.port);
        try {
            DataBaseManager dataBaseManager = new DataBaseManager();
            if (!dataBaseManager.clientExistInDB(registration.name) && registration.name != null) {
                dataBaseManager.saveClient(registration.name, registration.securedPassword);
                users.put(registration.name, connectionData);
                subtitlesPrinter.printLogGeneratedPassword();
                subtitlesPrinter.printLogClientRegistered(registration.name, connectionData.getInetAddress(), connectionData.getPort());
                Packet packetToSend = new Packet(messageHelper.registeredSuccessfully, connectionData);
                sendPacket(packetToSend);

            } else {
                if (registration.name == null) {
                    subtitlesPrinter.printLogClientRegistrationFailedCommand(registration.inetAddress, registration.port);
                } else {
                    subtitlesPrinter.printLogClientFailedRegistration(registration.name, connectionData.getInetAddress(), connectionData.getPort());
                }
                Packet packetToSend = new Packet(messageHelper.nicknameAlreadyTaken, connectionData);
                sendPacket(packetToSend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendUsersList(UsersListSender usersListSender) {
        subtitlesPrinter.printLogUsersListRequest();

        Packet packetToSend = new Packet(messageHelper.clientList(), new ConnectionData(usersListSender.inetAddress, usersListSender.port));
        sendPacket(packetToSend);
    }

    private void sendMessage(Messenger messenger) {
        try {
            DataBaseManager dataBaseManager = new DataBaseManager();

            if (dataBaseManager.clientExistInDB(messenger.receiver)) {
                byte[] messageToSend = stringToSendHelper(messenger.message, messenger.sender);
                subtitlesPrinter.printLogSuccessfullySentMessage(messenger.sender, messenger.receiver, messenger.message);
                Packet packetToSend = new Packet(messageToSend, new ConnectionData(messenger.destinationInetAddress, messenger.destinationPort));
                sendPacket(packetToSend);
            } else {
                subtitlesPrinter.printLogMessageNotSent(messenger.sender, messenger.receiver);
                sendPacket(new Packet(messageHelper.failedToSendMessage, new ConnectionData(messenger.destinationInetAddress, messenger.destinationPort)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loginUser(Login login) {
        ConnectionData connectionData = new ConnectionData(login.inetAddress, login.port);
        try {
            DataBaseManager dataBaseManager = new DataBaseManager();
            if (dataBaseManager.clientExistInDB(login.name)) {
                subtitlesPrinter.printLogClientFoundInDB(login.name);
                if (passwordHasher.checkIfPasswordMatches(login.name, login.password) && login.name != null) {
                    subtitlesPrinter.printLogClientLoggedIn(login.name);
                    users.put(login.name, connectionData);
                    Packet packetToSend = new Packet(messageHelper.successfullyLoggedIn(login.name), connectionData);
                    sendPacket(packetToSend);
                } else {
                    sendPacket(new Packet(messageHelper.failedLogin, connectionData));
                }
            } else {
                sendPacket(new Packet(messageHelper.failedLogin, connectionData));
                subtitlesPrinter.printLogClientDoesNotExist(login.name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logoutUser(Logout logout) {
        subtitlesPrinter.printLogClientLoggedOut(logout.name);
        users.remove(logout.name);
        Packet packetToSend = new Packet(messageHelper.loggedOut, new ConnectionData(logout.inetAddress, logout.port));
        sendPacket(packetToSend);
    }

    private byte[] stringToSendHelper(String text, String sender) {
        String textToSend = sender + ": " + text;
        return textToSend.getBytes(StandardCharsets.UTF_8);
    }
}
