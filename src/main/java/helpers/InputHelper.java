package helpers;

public class InputHelper {

    public String defineReceiver(String input) {
        String[] words = input.split(" ");
        return words[1];
    }

    public String defineWhoWantsToRegister(String input) {
        String[] words = input.split(" ");
        return words[1];
    }

    public String definePassword(String input){
        String[] words = input.split(" ");
        return words[1];
    }

    public String defineNickname(String input){
        String[] words = input.split(" ");
        return words[0];
    }

    public static String defineMessageFromInput(String input) {
        String[] words = input.split(" ", 3);
        return words[2];
    }
}
