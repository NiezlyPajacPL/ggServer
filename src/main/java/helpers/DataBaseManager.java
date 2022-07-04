package helpers;

import java.io.*;
import java.util.Scanner;

public class DataBaseManager {
    File registeredUsers = new File("src/main/java/managers/commands/RegisteredClients.txt");
    Scanner scanner = new Scanner(registeredUsers);

    FileWriter fileWriter = new FileWriter(registeredUsers.getAbsoluteFile(), true);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    InputHelper inputHelper = new InputHelper();

    public DataBaseManager() throws IOException {
    }

    public void saveClient(String nickname, SecuredPassword securedPassword){
        String data = nickname + " " + securedPassword.password + " " + securedPassword.salt;
        overrideDB(data);
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

    private void overrideDB(String data) {
        try {
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
