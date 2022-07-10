package managers;

import helpers.ClientLoginInfo;
import helpers.InputHelper;
import helpers.SecuredPassword;
import managers.DataBase;

import java.io.*;
import java.util.Scanner;

public class DataBaseImpl implements DataBase {
    File registeredUsers = new File("src/main/java/managers/commands/RegisteredClients.txt");
  //  Scanner scanner = new Scanner(registeredUsers);
    Scanner scanner;
    FileWriter fileWriter = new FileWriter(registeredUsers.getAbsoluteFile(), true);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    InputHelper inputHelper = new InputHelper();

    public DataBaseImpl() throws IOException {
    }
    @Override
    public ClientLoginInfo getClient(String nickname) {
        if(clientExistInDB(nickname)){
            return new ClientLoginInfo(nickname,getHashedPassword(nickname));
        }else{
            return null;
        }
    }

    @Override
    public void saveClient(ClientLoginInfo clientLoginInfo) {
        String data = clientLoginInfo.getNickname() + " " + clientLoginInfo.getSecuredPassword() + " " + clientLoginInfo.getSecuredPassword().salt;
        overrideDB(data);
    }

    public void saveClient(String nickname, SecuredPassword securedPassword){
        String data = nickname + " " + securedPassword.password + " " + securedPassword.salt;
        overrideDB(data);
    }

    private boolean clientExistInDB(String nickname) {
        try {
            scanner = new Scanner(registeredUsers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            String string = scanner.nextLine();
            String nicknameFromString = inputHelper.dataBaseDefineNickname(string);
            if (nicknameFromString.equals(nickname)) {
                return true;
            }
        }
        return false;
    }
    private SecuredPassword getHashedPassword(String nickname) {
        try {
            scanner = new Scanner(registeredUsers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
