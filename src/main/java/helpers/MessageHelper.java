package helpers;

public final class MessageHelper {
    public static String REGISTERED_SUCCESSFULLY = "Registered Successfully!";
    public static String NICKNAME_TAKEN = "Something went wrong or nickname is already taken. :(";
    public static String FAILED_LOGIN = "Something went wrong. Try again";
    public static String LOGGED_OUT = "Successfully logged out. See you soon!";
    public static String FAILED_TO_SEND_MESSAGE = "Message wasn't sent. The user you are trying to reach is offline or does not exist.";

    public static String successfullyLoggedIn(String name) {
        return "Hello again " + name + "!";
    }
    //ALLUSERS
    // public String clientList() {
    //     return users.keySet().toString();
    // }
}
