package helpers;

import managers.ConnectionData;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {
    File registeredUsers = new File("src/main/java/managers/commands/RegisteredClients.txt");
    Scanner scanner = new Scanner(registeredUsers);

    FileWriter fileWriter = new FileWriter(registeredUsers.getAbsoluteFile(), true);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    InputHelper inputHelper = new InputHelper();

    public FileHandler() throws IOException {
    }

    public boolean clientExistInDB(String nickname) {
        while (scanner.hasNextLine()) {
            String string = scanner.nextLine();
            String nicknameFromString = inputHelper.dataBaseDefineNickname(string);
            if (nicknameFromString.equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public SecuredPassword getHashedPassword(String nickname) {
        while (scanner.hasNextLine()) {
            String string = scanner.nextLine();
            String nicknameFromString = inputHelper.dataBaseDefineNickname(string);
            if (nicknameFromString.equals(nickname)) {
                return new SecuredPassword(inputHelper.dataBaseDefinePassword(string), inputHelper.getSaltFromDataBase(string));
            }
        }
        return null;
    }

    public void overrideDB(String data) {
        try {
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
