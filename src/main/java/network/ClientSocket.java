package network;

import helpers.*;
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
    DataBaseManager dataBaseManager = new DataBaseManager();
    UserRegistrationListener userRegistrationListener;


    public ClientSocket(Socket socket,MessageHelper messageHelper,UserRegistrationListener userRegistrationListener) throws IOException {
        this.socket = socket;
        this.messageHelper = messageHelper;
        this.userRegistrationListener = userRegistrationListener;
    }

    @Override
    public void run() {

        while (true) {
            CommandMapperImpl commandMapper = new CommandMapperImpl();
            MessageType messageType;
            Packet receivedPacket = receivePacket();
            messageType = commandMapper.mapCommand(receivedPacket);

            if (messageType instanceof Registration) {
                if (dataBaseManager.clientExistInDB(((Registration) messageType).name)) {
                    registerUser((Registration) messageType);
                    userRegistrationListener.onClientRegistered(((Registration) messageType).name);
                    sendPacket(new Packet(MessageHelper.REGISTERED_SUCCESSFULLY.getBytes(StandardCharsets.UTF_8), receivedPacket.getConnectionData()));
                } else {
                    //print log
                    sendPacket(new Packet(MessageHelper.NICKNAME_TAKEN.getBytes(StandardCharsets.UTF_8), receivedPacket.getConnectionData()));
                }
                //      sendPacket(commandHandler.registerUser((Registration) messageType));
            } else if (messageType instanceof UsersListSender) {
                sendPacket(commandHandler.sendUsersList((UsersListSender) messageType));
            } else if (messageType instanceof Messenger) {
                sendPacket(commandHandler.sendMessage((Messenger) messageType));
            } else if (messageType instanceof Login) {
                sendPacket(commandHandler.loginUser((Login) messageType));
            } else if (messageType instanceof Logout) {
                sendPacket(commandHandler.logoutUser((Logout) messageType));
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

            return new Packet(receivedString.getBytes(StandardCharsets.UTF_8), new ConnectionData(socket.getInputStream(), socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void registerUser(Registration registration) {
        SecuredPassword securedPassword = passwordHasher.generateSecuredPassword(registration.password);
        Logger.printLogGeneratedPassword();
        dataBaseManager.saveClient(registration.name, securedPassword);
    }
}


