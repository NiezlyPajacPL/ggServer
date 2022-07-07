package managers.commands;

import helpers.*;
import managers.ConnectionData;
import managers.Logger;
import managers.commands.messageTypes.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CommandHandler {
    Logger logger;
    MessageHelper messageHelper;
    Map<String, ConnectionData> users;
    PasswordHasher passwordHasher = new PasswordHasher();
    DataBaseManager dataBaseManager;

    {
        try {
            dataBaseManager = new DataBaseManager();
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
        dataBaseManager.saveClient(registration.name, securedPassword);

        users.put(registration.name, userConnectionData);
        logger.printLogGeneratedPassword();
        logger.printLogClientRegistered(registration.name, userConnectionData);
    }
    // if (!dataBaseManager.clientExistInDB(registration.name) && registration.name != null) {
    // }
    public Packet sendUsersList(UsersListSender usersListSender) {
        logger.printLogUsersListRequest();

        return new Packet(messageHelper.clientList().getBytes(StandardCharsets.UTF_8), usersListSender.connectionData);
    }

    public Packet sendMessage(Messenger messenger) {
        Packet packetToSend;
        if (dataBaseManager.clientExistInDB(messenger.receiver)) {
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
        if (dataBaseManager.clientExistInDB(login.name)) {
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
