package network;

import com.google.gson.Gson;
import helpers.*;
import helpers.MessageListener;
import managers.DataBase;
import helpers.Logger;
import managers.PasswordHandler;
import managers.commands.CommandMapper;
import managers.commands.CommandMapperImpl;
import managers.commands.messageTypes.*;
import managers.jsonObj.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ClientSocket implements Server {
    private final Socket socket;
    private final PasswordHandler passwordHandler;
    private final MessageListener messageListener;
    private final DataBase db;
    private String clientName;
    private final CommandMapper commandMapper = new CommandMapperImpl();
    private final Gson gson = new Gson();

    public ClientSocket(Socket socket, MessageListener messageListener, DataBase db, PasswordHandler passwordHandler) throws IOException {
        this.socket = socket;
        this.messageListener = messageListener;
        this.db = db;
        this.passwordHandler = passwordHandler;
    }

    @Override
    public void run() {
        while (true) {
            MessageType messageType;
            Packet receivedPacket = receivePacket();
            if (receivedPacket == null) {
                break;
            }
            String json;
            messageType = commandMapper.mapCommand(receivedPacket);

            if (messageType instanceof Registration) {
                clientName = ((Registration) messageType).name;
                RegisterData registerData;

                if (!clientExistInDB()) {
                    registerUser((Registration) messageType);
                    messageListener.onClientLoggingIn(((Registration) messageType).name);
                    Logger.printLogClientRegistered(clientName, socket);
                    registerData = new RegisterData(Type.REGISTER, true);

                } else {
                    Logger.printLogClientFailedRegistration(((Registration) messageType).name, socket);
                    registerData = new RegisterData(Type.REGISTER, false);
                }

                sendPacket(new Packet(gson.toJson(registerData).getBytes(StandardCharsets.UTF_8), socket));
            } else if (messageType instanceof Login) {
                LoginData loginData;
                clientName = ((Login) messageType).name;
                String password = ((Login) messageType).password;
                if (clientExistInDB()) {
                    Logger.printLogClientFoundInDB(clientName);
                    if (passwordHandler.isPasswordValid(clientName, password)) {
                        messageListener.onClientLoggingIn(clientName);
                        Logger.printLogClientLoggedIn(clientName, socket);
                        loginData = new LoginData(Type.LOGIN, true);
                    } else {
                        Logger.printLogClientFailedLogin(clientName, socket);
                        loginData = new LoginData(Type.LOGIN, false);
                    }
                } else {
                    Logger.printLogClientFailedLogin(clientName, socket);
                    loginData = new LoginData(Type.LOGIN, false);
                }

                sendPacket(new Packet(gson.toJson(loginData).getBytes(StandardCharsets.UTF_8), socket));
            } else if (messageType instanceof UsersListSender) {
                Logger.printLogUsersListRequest();
                json = gson.toJson(new OnlineUsersData(Type.ONLINE_USERS, new ArrayList<>(messageListener.getUsersList().keySet())));

                sendPacket(new Packet(json.getBytes(StandardCharsets.UTF_8), socket));
            } else if (messageType instanceof Message) {
                String receiver = ((Message) messageType).receiver;
                String messageReceived = ((Message) messageType).message;

                if (userIsOnline(receiver)) {
                    String textToSend = clientName + ": " + messageReceived;
                    json = gson.toJson(new MessageData(Type.MESSAGE, clientName, textToSend));
                    Logger.printLogSuccessfullySentMessage(clientName, receiver, messageReceived);

                    sendPacket(new Packet(json.getBytes(StandardCharsets.UTF_8), messageListener.onMessageReceivedGetUser(receiver)));
                } else {
                    Logger.printLogMessageNotSent(clientName, receiver);
                    String message = "User you are trying to reach is currently offline.";
                    json = gson.toJson(new MessageData(Type.MESSAGE, receiver, message));

                    sendPacket(new Packet(json.getBytes(StandardCharsets.UTF_8), socket));
                }
            } else if (messageType instanceof Logout) {
                messageListener.onClientLoggedOut(clientName);
                LogoutData logoutData = new LogoutData(Type.LOGOUT, MessageHelper.LOGGED_OUT);

                sendPacket(new Packet(gson.toJson(logoutData).getBytes(StandardCharsets.UTF_8), socket));
                break;
            }
        }
        messageListener.onClientLoggedOut(clientName);
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
        SecuredPassword securedPassword = passwordHandler.generateSecuredPassword(registration.password);
        Logger.printLogGeneratedPassword();
      //  String uniqueID = UUID.randomUUID().toString();
        db.saveClient(new ClientLoginInfo(registration.name, securedPassword));
    }

    private void stopConnection() {
        if (clientName == null) {
            Logger.printLogConnectionInterrupted(socket.getInetAddress(), socket.getPort());
        } else {
            Logger.printLogClientLoggedOut(clientName);
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean clientExistInDB() {
        return db.getClient(clientName) != null;
    }

    private boolean userIsOnline(String user) {
        return messageListener.getUsersList().get(user) != null;
    }
}