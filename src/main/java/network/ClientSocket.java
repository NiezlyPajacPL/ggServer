package network;

import helpers.*;
import helpers.Listeners.MessageListener;
import managers.ConnectionData;
import managers.Logger;
import managers.commands.CommandMapperImpl;
import managers.commands.messageTypes.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientSocket implements Server {
    private Socket socket;
    private PrintWriter messageSender;
    private MessageHelper messageHelper;
    PasswordHasher passwordHasher = new PasswordHasher();
    MessageListener messageListener;
    DataBaseManager dataBaseManager;
    String clientName;

    public ClientSocket(Socket socket, MessageHelper messageHelper, MessageListener messageListener) throws IOException {
        this.socket = socket;
        this.messageHelper = messageHelper;
        this.messageListener = messageListener;
    }

    @Override
    public void run() {

        while (true) {
            CommandMapperImpl commandMapper = new CommandMapperImpl();
            MessageType messageType;
            Packet receivedPacket = receivePacket();
            messageType = commandMapper.mapCommand(receivedPacket);
            try {
                dataBaseManager = new DataBaseManager();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (messageType instanceof Registration) {
                if (!dataBaseManager.clientExistInDB(((Registration) messageType).name)) {
                    registerUser((Registration) messageType);
                    clientName = ((Registration) messageType).name;
                    messageListener.onClientLoggingIn(((Registration) messageType).name);
                    sendPacket(new Packet(MessageHelper.REGISTERED_SUCCESSFULLY.getBytes(StandardCharsets.UTF_8), receivedPacket.getConnectionData()));
                } else {
                    Logger.printLogClientFailedRegistration(((Registration) messageType).name);
                    sendPacket(new Packet(MessageHelper.NICKNAME_TAKEN.getBytes(StandardCharsets.UTF_8), receivedPacket.getConnectionData()));
                }

            } else if (messageType instanceof UsersListSender) {
                byte[] message = messageListener.onUsersListRequest().getBytes(StandardCharsets.UTF_8);
                try {
                    sendPacket(new Packet(message, new ConnectionData(socket.getOutputStream())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (messageType instanceof Messenger) {
                String receiver = ((Messenger) messageType).receiver;
                String messageReceived = ((Messenger) messageType).message;
                if (dataBaseManager.clientExistInDB(receiver)) {
                    byte[] messageToSend = prepareStringToSend(clientName, messageReceived);
                    try {
                        sendPacket(new Packet(messageToSend,new ConnectionData(messageListener.onMessageReceivedGetReceiverSocket(receiver).getOutputStream())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Logger.printLogSuccessfullySentMessage(clientName, receiver, messageReceived);
                } else {
                    Logger.printLogMessageNotSent(clientName, receiver);
                }
            } else if (messageType instanceof Login) {
                String name = ((Login) messageType).name;
                if (dataBaseManager.clientExistInDB(name)) {
                    Logger.printLogClientFoundInDB(name);
                    if (passwordHasher.checkIfPasswordMatches((name), ((Login) messageType).password) && (name != null)) {
                        clientName = name;
                        Logger.printLogClientLoggedIn(name);
                        messageListener.onClientLoggingIn(name);
                        sendPacket(new Packet(MessageHelper.successfullyLoggedIn(name).getBytes(StandardCharsets.UTF_8),
                                receivedPacket.getConnectionData()));
                    } else {
                        sendPacket(new Packet(MessageHelper.FAILED_LOGIN.getBytes(StandardCharsets.UTF_8),
                                receivedPacket.getConnectionData()));
                    }
                } else {
                    sendPacket(new Packet(MessageHelper.FAILED_LOGIN.getBytes(StandardCharsets.UTF_8),
                            receivedPacket.getConnectionData()));
                }
            } else if (messageType instanceof Logout) {
                //   sendPacket(commandHandler.logoutUser((Logout) messageType));
                break;
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPacket(Packet packetToSend) {
        messageSender = new PrintWriter(packetToSend.getConnectionData().getSendingStream(), true);
        messageSender.println(new String(packetToSend.getData()));
    }

    @Override
    public Packet receivePacket() {
        try {
            BufferedReader receivedBufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String receivedString = receivedBufferReader.readLine();

            return new Packet(receivedString.getBytes(StandardCharsets.UTF_8), new ConnectionData(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void registerUser(Registration registration) {
        SecuredPassword securedPassword = passwordHasher.generateSecuredPassword(registration.password);
        Logger.printLogGeneratedPassword();
        dataBaseManager.saveClient(registration.name, securedPassword);
    }

    private byte[] prepareStringToSend(String sender, String message) {
        String textToSend = sender + ": " + message;
        return textToSend.getBytes(StandardCharsets.UTF_8);
    }

}

