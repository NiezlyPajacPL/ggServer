package helpers;

import managers.ConnectionData;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MessageHelper {
    Map<String, ConnectionData> users;

    public MessageHelper(Map<String, ConnectionData> users) {
        this.users = users;
    }

    //REGISTER
    public byte[] registeredSuccessfully = "Registered Successfully!".getBytes(StandardCharsets.UTF_8);
    public byte[] nicknameAlreadyTaken = "Something went wrong or nickname is already taken. :(".getBytes(StandardCharsets.UTF_8);

    //LOGIN
    public byte[] failedLogin = "Something went wrong. Try again".getBytes(StandardCharsets.UTF_8);
    public byte[] successfullyLoggedIn(String name) {
        String message = "Hello again " + name + "!";
        return message.getBytes(StandardCharsets.UTF_8);
    }

    //LOGOUT
    public byte[] loggedOut = "Successfully logged out. See you soon!".getBytes(StandardCharsets.UTF_8);

    //MESSAGES
    public byte[] failedToSendMessage = "Message wasn't sent. The user you are trying to reach is offline or does not exist.".getBytes(StandardCharsets.UTF_8);

    //ALLUSERS
    public byte[] clientList() {
        return users.keySet().toString().getBytes(StandardCharsets.UTF_8);
    }
}
