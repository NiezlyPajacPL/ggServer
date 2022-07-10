package helpers;

import helpers.DateAndTime;

import java.net.Socket;

public final class Logger {

    //SERVER
    public static void printLogServerStarted(){println(DateAndTime.getCurrentTime() + ": SERVER HAS STARTED.");}

    //REGISTRATION
    public static void printLogClientRegistered(String nickname, Socket socket){println(DateAndTime.getCurrentTime() +
            ": Registered client " + nickname + " -- IP: " + socket.getInetAddress() + ":" + socket.getPort());}

    public static void printLogClientFailedRegistration(String nickname, Socket socket){println(DateAndTime.getCurrentTime() +
            "Someone with ip: " + socket.getInetAddress() + ":" + socket.getPort()+ "tried to register on used nickname " + "(" + nickname + ")");}

    public static void printLogClientRegistrationFailedCommand(Socket socket){println(DateAndTime.getCurrentTime() +
            ": Someone with IP:   "  + socket.getInetAddress() + ":" + socket.getPort() + " -- tried to register but used command wrongly.");}

    public static void printLogGeneratedPassword(){println(DateAndTime.getCurrentTime() + ": Generated secured password.");}

    //LOGIN
    public static void printLogClientLoggedIn(String nickname, Socket socket){println(DateAndTime.getCurrentTime() +
            ": Client - " + nickname + " successfully logged in. IP: " + socket.getInetAddress() + ":" + socket.getPort() );}

    public static void printLogClientLoggedOut(String nickname){println(DateAndTime.getCurrentTime() + ": Client: " + nickname + " has logged out. Socket has been closed.");}

    public static void printLogClientDoesNotExist(String name){ println(DateAndTime.getCurrentTime() + ": " +  name + " does NOT exist in DB");}

    public static void printLogClientFailedLogin(String name, Socket socket){ println(DateAndTime.getCurrentTime() +
            ": " +"IP -- " + socket.getInetAddress() + ":" + socket.getPort() + " unsuccessfully tried to login on account with name "+ "[" + name + "]" );}

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
