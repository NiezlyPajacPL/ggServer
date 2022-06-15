package managers;

import helpers.DateAndTime;
import java.net.InetAddress;


public class SubtitlesPrinter {

    DateAndTime dateAndTime = new DateAndTime();

    public void printLogServerStarted(){println(dateAndTime.getCurrentTime() + ": SERVER HAS STARTED.");}

    public void printLogSomeoneTriedToSendMessage(){println(dateAndTime.getCurrentTime() + ": Someone tried to send message.");}

    public void printLogUsersListRequest(){println(dateAndTime.getCurrentTime() + ": Received users list request.");}

    public void printLogClientLoggedIn(String nickname){println(dateAndTime.getCurrentTime() + ": Client - " + nickname + " successfully logged in." );}

    public void printLogClientRegistered(String nickname, InetAddress inetAddress, int port){println(dateAndTime.getCurrentTime() + ": Registered client " + nickname + " -- IP: " + inetAddress + ":" + port);}

    public void printLogSuccessfullySentMessage(String sender, String receiver){println(dateAndTime.getCurrentTime() + ": Client: " + sender + " successfully sent message to:  " + receiver);}

    public void printLogMessageWasNotSent(){println(dateAndTime.getCurrentTime() + ": Message wasn't sent.");}

    private void println(String text){System.out.println(text);}

    private void print(String text){System.out.print(text);}
}
