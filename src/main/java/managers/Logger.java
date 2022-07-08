package managers;

import helpers.DateAndTime;


public final class Logger {

    //SERVER
    public static void printLogServerStarted(){println(DateAndTime.getCurrentTime() + ": SERVER HAS STARTED.");}

    //REGISTRATION
    public static void printLogClientRegistered(String nickname, ConnectionData connectionData){println(DateAndTime.getCurrentTime() +
            ": Registered client " + nickname + " -- IP: " + connectionData.getInetAddress() + ":" + connectionData.getPort());}

    public static void printLogClientFailedRegistration(String nickname){println(DateAndTime.getCurrentTime() + " Someone tried to register on used nickname " + "(" + nickname + ")");}

    public static void printLogClientRegistrationFailedCommand(ConnectionData connectionData){println(DateAndTime.getCurrentTime() +
            ": Someone with IP:   "  + connectionData.getInetAddress() + ":" + connectionData.getPort() + " -- tried to register but used command wrongly.");}

    public static void printLogGeneratedPassword(){println(DateAndTime.getCurrentTime() + ": Generated secured password.");}

    //LOGIN
    public static void printLogClientLoggedIn(String nickname){println(DateAndTime.getCurrentTime() + ": Client - " + nickname + " successfully logged in." );}

    public static void printLogClientLoggedOut(String nickname){println(DateAndTime.getCurrentTime() + ": Client: " + nickname + " has logged out.");}

    public static void printLogClientDoesNotExist(String name){ println(DateAndTime.getCurrentTime() +" " +  name + " does NOT exist in DB");}

    public static void printLogClientFoundInDB(String nickname){println(DateAndTime.getCurrentTime() + " Client " + nickname + " found in data base");}

    //OTHERS

    public static void printLogUsersListRequest(){println(DateAndTime.getCurrentTime() + ": Received users list request.");}

    public static void printLogSuccessfullySentMessage(String sender, String receiver,String message){println(DateAndTime.getCurrentTime() +
            ": Client: " + sender + " successfully sent message to:  " + receiver + "/ Content: " + message);}

    public static void printLogMessageNotSent(String sender, String receiver){println(DateAndTime.getCurrentTime() + ": Client unsuccessfully tried to send message to " + receiver);}

    //PRIVATE
    private static void println(String text){System.out.println(text);}

    private static void print(String text){System.out.print(text);}
}
