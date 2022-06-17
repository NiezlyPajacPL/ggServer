package helpers;

public class InputHelper {

    public String defineReceiver(String input) {
        String[] words = input.split(" ");
        return words[1];
    }

    public static String defineWhoWantsToRegister(String input) {
        String[] words = input.split(" ");
        return words[1];
    }
    public static String defineMessageFromInput(String input) {
        String[] words = input.split(" ", 3);
        return words[2];
    }

    public String definePasswordFromInput(String input){
        String[] words = input.split(" ");
        return words[2];
    }

    public String dataBaseDefinePassword(String input){
        String[] words = input.split(" ");
        return words[1];
    }

    public String dataBaseDefineNickname(String input){
        String[] words = input.split(" ");
        return words[0];
    }
}
