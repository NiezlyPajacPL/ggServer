package managers.commands;

import helpers.DataBaseManager;
import helpers.MessageHelper;
import helpers.Packet;
import helpers.PasswordHasher;
import managers.ConnectionData;
import managers.SubtitlesPrinter;
import managers.commands.messageTypes.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Commands {
    SubtitlesPrinter subtitlesPrinter;
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

    public Commands( SubtitlesPrinter subtitlesPrinter, MessageHelper messageHelper, Map<String, ConnectionData> users) {
        this.subtitlesPrinter = subtitlesPrinter;
        this.messageHelper = messageHelper;
        this.users = users;
    }

    public Packet registerUser(Registration registration) {
        Packet packetToSend;

        if (!dataBaseManager.clientExistInDB(registration.name) && registration.name != null) {
            dataBaseManager.saveClient(registration.name, registration.securedPassword);
            users.put(registration.name, registration.connectionData);
            subtitlesPrinter.printLogGeneratedPassword();
            subtitlesPrinter.printLogClientRegistered(registration.name, registration.connectionData);
            packetToSend = new Packet(messageHelper.registeredSuccessfully.getBytes(StandardCharsets.UTF_8), registration.connectionData);

        } else {
            if (registration.name == null) {
                subtitlesPrinter.printLogClientRegistrationFailedCommand(registration.connectionData);
            } else {
                subtitlesPrinter.printLogClientFailedRegistration(registration.name, registration.connectionData);
            }
            packetToSend = new Packet(messageHelper.nicknameAlreadyTaken.getBytes(StandardCharsets.UTF_8), registration.connectionData);
        }
        return packetToSend;
    }

    public Packet sendUsersList(UsersListSender usersListSender) {
        subtitlesPrinter.printLogUsersListRequest();

        return new Packet(messageHelper.clientList().getBytes(StandardCharsets.UTF_8), usersListSender.connectionData);
    }

    public Packet sendMessage(Messenger messenger) {
        Packet packetToSend;
        if (dataBaseManager.clientExistInDB(messenger.receiver)) {
            byte[] messageToSend = stringToSendHelper(messenger.message, messenger.sender);
            subtitlesPrinter.printLogSuccessfullySentMessage(messenger.sender, messenger.receiver, messenger.message);
            packetToSend = new Packet(messageToSend, messenger.connectionData);
        } else {
            subtitlesPrinter.printLogMessageNotSent(messenger.sender, messenger.receiver);
            packetToSend = new Packet(messageHelper.failedToSendMessage.getBytes(StandardCharsets.UTF_8), messenger.connectionData);
        }
        return packetToSend;
    }

    public Packet loginUser(Login login) {
        Packet packetToSend;
        if (dataBaseManager.clientExistInDB(login.name)) {
            subtitlesPrinter.printLogClientFoundInDB(login.name);
            if (passwordHasher.checkIfPasswordMatches(login.name, login.password) && login.name != null) {
                subtitlesPrinter.printLogClientLoggedIn(login.name);
                users.put(login.name, login.connectionData);
                packetToSend = new Packet(messageHelper.successfullyLoggedIn(login.name).getBytes(StandardCharsets.UTF_8), login.connectionData);
            } else {
                packetToSend = new Packet(messageHelper.failedLogin.getBytes(StandardCharsets.UTF_8), login.connectionData);
            }
        } else {
            packetToSend = new Packet(messageHelper.failedLogin.getBytes(StandardCharsets.UTF_8), login.connectionData);
            subtitlesPrinter.printLogClientDoesNotExist(login.name);
        }
        return packetToSend;
    }

    public Packet logoutUser(Logout logout) {
        subtitlesPrinter.printLogClientLoggedOut(logout.name);
        users.remove(logout.name);
        return new Packet(messageHelper.loggedOut.getBytes(StandardCharsets.UTF_8), logout.connectionData);
    }

    private byte[] stringToSendHelper(String text, String sender) {
        String textToSend = sender + ": " + text;
        return textToSend.getBytes(StandardCharsets.UTF_8);
    }
}
