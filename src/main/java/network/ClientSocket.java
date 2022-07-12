package network;

import helpers.*;
import helpers.MessageListener;
import managers.DataBaseImpl;
import helpers.Logger;
import managers.PasswordHasher;
import managers.commands.CommandMapperImpl;
import managers.commands.messageTypes.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientSocket implements Server {
    private final Socket socket;
    private final PasswordHasher passwordHasher = new PasswordHasher();
    private final MessageListener messageListener;
    private DataBaseImpl dataBaseImpl;
    private String clientName;

    public ClientSocket(Socket socket, MessageListener messageListener) throws IOException {
        this.socket = socket;
        this.messageListener = messageListener;
    }

    @Override
    public void run() {

        while (true) {
            CommandMapperImpl commandMapper = new CommandMapperImpl();
            MessageType messageType;
            Packet receivedPacket = receivePacket();
            if(receivedPacket == null){
                break;
            }
            messageType = commandMapper.mapCommand(receivedPacket);
            try {
                dataBaseImpl = new DataBaseImpl();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (messageType instanceof Registration) {
                if (dataBaseImpl.getClient(((Registration) messageType).name) == null) {
                    registerUser((Registration) messageType);
                    clientName = ((Registration) messageType).name;
                    messageListener.onClientLoggingIn(((Registration) messageType).name);
                    sendPacket(new Packet(MessageHelper.REGISTERED_SUCCESSFULLY.getBytes(StandardCharsets.UTF_8), socket));
                } else {
                    Logger.printLogClientFailedRegistration(((Registration) messageType).name,socket);
                    sendPacket(new Packet(MessageHelper.NICKNAME_TAKEN.getBytes(StandardCharsets.UTF_8), socket));
                }

            } else if (messageType instanceof UsersListSender) {
                byte[] message = messageListener.onUsersListRequest().getBytes(StandardCharsets.UTF_8);
                sendPacket(new Packet(message, socket));

            } else if (messageType instanceof Messenger) {
                String receiver = ((Messenger) messageType).receiver;
                String messageReceived = ((Messenger) messageType).message;
                if (dataBaseImpl.getClient(receiver) != null) {
                    byte[] messageToSend = prepareStringToSend(clientName, messageReceived);
                    sendPacket(new Packet(messageToSend, messageListener.onMessageReceivedGetReceiverSocket(receiver)));
                    Logger.printLogSuccessfullySentMessage(clientName, receiver, messageReceived);
                } else {
                    Logger.printLogMessageNotSent(clientName, receiver);
                    sendPacket(new Packet(MessageHelper.FAILED_TO_SEND_MESSAGE.getBytes(StandardCharsets.UTF_8),socket));
                }
            } else if (messageType instanceof Login) {
                String name = ((Login) messageType).name;
                if (dataBaseImpl.getClient(name) != null) {
                    Logger.printLogClientFoundInDB(name);
                    if (passwordHasher.checkIfPasswordMatches((name), ((Login) messageType).password)) {
                        clientName = name;
                        Logger.printLogClientLoggedIn(name,socket);
                        messageListener.onClientLoggingIn(name);
                        sendPacket(new Packet(MessageHelper.successfullyLoggedIn(name).getBytes(StandardCharsets.UTF_8), socket));
                    } else {
                        Logger.printLogClientFailedLogin(name, socket);
                        sendPacket(new Packet(MessageHelper.WRONG_PASSWORD.getBytes(StandardCharsets.UTF_8), socket));
                    }
                } else {
                    sendPacket(new Packet(MessageHelper.FAILED_LOGIN.getBytes(StandardCharsets.UTF_8),socket));
                }
            } else if (messageType instanceof Logout) {
                messageListener.onClientLoggedOut(clientName);
                sendPacket(new Packet(MessageHelper.LOGGED_OUT.getBytes(StandardCharsets.UTF_8), socket));
                break;
            }
        }
        stopConnection();
    }

    @Override
    public void sendPacket(Packet packetToSend) {
        try {
            PrintWriter messageSender = new PrintWriter(packetToSend.getSocket().getOutputStream(), true);
            messageSender.println(new String(packetToSend.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Packet receivePacket() {
        try {
            BufferedReader receivedBufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String receivedString = receivedBufferReader.readLine();

            return new Packet(receivedString.getBytes(StandardCharsets.UTF_8), socket);
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private void registerUser(Registration registration) {
        SecuredPassword securedPassword = passwordHasher.generateSecuredPassword(registration.password);
        Logger.printLogGeneratedPassword();
        dataBaseImpl.saveClient(registration.name, securedPassword);
    }

    private byte[] prepareStringToSend(String sender, String message) {
        String textToSend = sender + ": " + message;
        return textToSend.getBytes(StandardCharsets.UTF_8);
    }

    private void stopConnection(){
        if(clientName == null){
            Logger.printLogConnectionInterrupted(socket.getInetAddress(),socket.getPort());
        }else{
            Logger.printLogClientLoggedOut(clientName);
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

