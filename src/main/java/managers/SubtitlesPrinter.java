package managers;

import java.util.ArrayList;

public class SubtitlesPrinter {

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

    public void printMessage(String message,String receiver){println("Do: " + receiver + "> " + message);}

    public void printIsHelpNeeded(){println("Jesli potrzebujesz pomocy wpisz '/help'");}

    public void printHello(String nickname){println("Hello, " + nickname + "! ");}

    public void askForNickname(){print("Prosze podac swoj nick: ");}

    public void printSomethingWentWrong(){println("Cos poszlo nie tak, lub nie zdefiniowales do kogo probujesz wyslac wiadomosc. Uzyj '/help'");}

    public void goodBye(){println("Nara");}

    private void println(String text){System.out.println(text);}

    private void print(String text){System.out.print(text);}
}
