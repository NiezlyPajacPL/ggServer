package helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {
    File registeredUsers = new File("src\\main\\java\\managers\\commandHandlers\\RegisteredClients.txt");
    Scanner scanner = new Scanner(registeredUsers);

    ArrayList<String> fileDataInList = new ArrayList<>();
    BufferedReader bufferedReader = new BufferedReader(new FileReader(registeredUsers));
    FileWriter fileWriter = new FileWriter(registeredUsers.getAbsoluteFile(),true);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    InputHelper inputHelper = new InputHelper();

    public FileHandler() throws IOException {}

    public ArrayList<String> getUsersDataBase(){
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            fileDataInList.add(data);
        }
        System.out.println(fileDataInList);
        return fileDataInList;
    }

    public boolean doesClientExistInDataBase(String nickname){
        while(scanner.hasNextLine()) {
            String string = scanner.nextLine();
            String nicknameFromString = inputHelper.defineNickname(string);
            if(nicknameFromString.equals(nickname))

                System.out.println("Client " +nickname + " found in data base");
                return true;

        }
        return false;
    }

    public boolean doesPasswordMatch(String password){
        while(scanner.hasNextLine()) {
            String string = scanner.nextLine();
            String passwordFromString = inputHelper.definePassword(string);
            if(passwordFromString.equals(password))

                System.out.println("Password  matches");
            return true;

        }
        return false;
    }

    public void overrideDataBase(String data){
        try {
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
