package managers;

import helpers.DateAndTime;

import java.net.InetAddress;
import java.util.ArrayList;

public class SubtitlesPrinter {

    DateAndTime dateAndTime = new DateAndTime();

    public void printEnter(int enterCount){
        for(int i = 0; i < enterCount; i++){
            println("");
        }
    }

    public void printHelper(){
        println("////////////POMOC////////////");
        println("Lista wszystkich uzytkownikow: /allUsers");
        println("Wysylanie wiadomosci do danego uzytownika: /msg <nick uzytkownika> <twoja wiadomosc>");
        println("Wysylanie wiadomosci do poprzedniego uzytkownika: /r <twoja wiadomosc>");
        println("Wyjscie: /end");
    }
    public void printClientList(ArrayList<String> clientList){
        for(int i = 0; i < clientList.size(); i++) {
            println(i+1 + ". " + clientList.get(i));
        }
    }

    public void printLogServerStarted(){println(dateAndTime.getCurrentTime() + ": SERVER HAS STARTED.");}

    public void printLogSomeoneTriedToSendMessage(){println(dateAndTime.getCurrentTime() + ": Someone tried to send message.");}

    public void printLogUsersListRequest(){println(dateAndTime.getCurrentTime() + ": Received users list request.");}

    public void printLogClientRegistered(String nickname, InetAddress inetAddress, int port){println(dateAndTime.getCurrentTime() + ": Registered client " + nickname + " -- IP: " + inetAddress + ":" + port);}

    public void printLogSuccessfullySentMessage(String sender, String receiver){println(dateAndTime.getCurrentTime() + ": Client: " + sender + " successfully sent message to:  " + receiver);}

    public void printLogMessageWasNotSent(){println(dateAndTime.getCurrentTime() + ": Message wasn't sent.");}

    private void println(String text){System.out.println(text);}

    private void print(String text){System.out.print(text);}
}
