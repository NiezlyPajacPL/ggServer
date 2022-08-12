package network;

import helpers.MessageHelper;
import helpers.ConnectionData;
import helpers.Logger;
import helpers.Packet;
import managers.PasswordHasher;
import managers.commands.CommandMapperImpl;
import managers.commands.messageTypes.MessageType;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class UdpServer {
/*    private DatagramSocket socket;
    Logger logger;
    Map<String, ConnectionData> users = new HashMap<>();
    MessageHelper messageHelper = new MessageHelper();
    PasswordHasher passwordHasher;

    public UdpServer(int port, Logger logger,PasswordHasher passwordHasher) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.passwordHasher = passwordHasher;
        this.logger = logger;
    }

    public void run() {
        while (true) {
            CommandMapperImpl commandMapper = new CommandMapperImpl();
            MessageType messageType;
            Packet receivedPacket = receivePacket();
            messageType = commandMapper.mapCommand(receivedPacket);

            if (messageType instanceof Registration) {
                commandHandler.registerUser((Registration) messageType,receivedPacket.getConnectionData());
                sendPacket(commandHandler.registerUser((Registration) messageType),receivedPacket.getConnectionData());
            } else if (messageType instanceof UsersListSender) {
                sendPacket(commandHandler.sendUsersList((UsersListSender) messageType));
            } else if (messageType instanceof Message) {
                sendPacket(commandHandler.sendMessage((Message) messageType));
            }else if (messageType instanceof Login) {
                sendPacket(commandHandler.loginUser((Login) messageType));
            } else if (messageType instanceof Logout) {
                sendPacket(commandHandler.logoutUser((Logout) messageType));
            }
        }*/

/*
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


 */
    }
       /* Logger logger;
    MessageHelper messageHelper;
    Map<String, ConnectionData> users;
    PasswordHasher passwordHasher = new PasswordHasher();
    DataBaseImpl dataBaseImpl;

    {
        try {
            dataBaseImpl = new DataBaseImpl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean registeredUser;




    public CommandHandler(Logger logger, MessageHelper messageHelper, Map<String, ConnectionData> users) {
        this.logger = logger;
        this.messageHelper = messageHelper;
        this.users = users;
    }

    public void registerUser(Registration registration, ConnectionData userConnectionData) {
        SecuredPassword securedPassword = passwordHasher.generateSecuredPassword(registration.password);
        dataBaseImpl.saveClient(registration.name, securedPassword);

        users.put(registration.name, userConnectionData);
        logger.printLogGeneratedPassword();
        logger.printLogClientRegistered(registration.name, userConnectionData);
    }
    // if (!dataBaseImpl.clientExistInDB(registration.name) && registration.name != null) {
    // }
    public Packet sendUsersList(UsersListSender usersListSender) {
        logger.printLogUsersListRequest();

        return new Packet(messageHelper.clientList().getBytes(StandardCharsets.UTF_8), usersListSender.connectionData);
    }

    public Packet sendMessage(Message messenger) {
        Packet packetToSend;
        if (dataBaseImpl.clientExistInDB(messenger.receiver)) {
            byte[] messageToSend = stringToSendHelper(messenger.message, messenger.sender);
            logger.printLogSuccessfullySentMessage(messenger.sender, messenger.receiver, messenger.message);
            packetToSend = new Packet(messageToSend, messenger.connectionData);
        } else {
            logger.printLogMessageNotSent(messenger.sender, messenger.receiver);
            packetToSend = new Packet(messageHelper.FAILED_TO_SEND_MESSAGE.getBytes(StandardCharsets.UTF_8), messenger.connectionData);
        }
        return packetToSend;
    }

    public Packet loginUser(Login login) {
        Packet packetToSend;
        if (dataBaseImpl.clientExistInDB(login.name)) {
            logger.printLogClientFoundInDB(login.name);
            if (passwordHasher.checkIfPasswordMatches(login.name, login.password) && login.name != null) {
                logger.printLogClientLoggedIn(login.name);
                users.put(login.name, login.connectionData);
                packetToSend = new Packet(messageHelper.successfullyLoggedIn(login.name).getBytes(StandardCharsets.UTF_8), login.connectionData);
            } else {
                packetToSend = new Packet(messageHelper.FAILED_LOGIN.getBytes(StandardCharsets.UTF_8), login.connectionData);
            }
        } else {
            packetToSend = new Packet(messageHelper.FAILED_LOGIN.getBytes(StandardCharsets.UTF_8), login.connectionData);
            logger.printLogClientDoesNotExist(login.name);
        }
        return packetToSend;
    }

    public Packet logoutUser(Logout logout) {
        logger.printLogClientLoggedOut(logout.name);
        users.remove(logout.name);
        return new Packet(messageHelper.LOGGED_OUT.getBytes(StandardCharsets.UTF_8), logout.connectionData);
    }

    private byte[] stringToSendHelper(String text, String sender) {
        String textToSend = sender + ": " + text;
        return textToSend.getBytes(StandardCharsets.UTF_8);
    }


}
*/
