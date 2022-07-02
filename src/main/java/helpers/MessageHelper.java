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
    public String registeredSuccessfully = "Registered Successfully!";
    public String nicknameAlreadyTaken = "Something went wrong or nickname is already taken. :(";

    //LOGIN
    public String failedLogin = "Something went wrong. Try again";
    public String successfullyLoggedIn(String name) {
        return  "Hello again " + name + "!";
    }

    //LOGOUT
    public String loggedOut = "Successfully logged out. See you soon!";

    //MESSAGES
    public String failedToSendMessage = "Message wasn't sent. The user you are trying to reach is offline or does not exist.";

    //ALLUSERS
    public String clientList() {
        return users.keySet().toString();
    }
}
