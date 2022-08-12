package managers;

import helpers.ClientLoginInfo;
import helpers.InputHelper;
import helpers.SecuredPassword;

import java.io.*;
import java.util.Scanner;

public class DataBaseImpl implements DataBase {
    private String filePath;
    File registeredUsersFile;
    Scanner scanner;
    InputHelper inputHelper = new InputHelper();
    BufferedWriter bufferedWriter;

    public DataBaseImpl(String filePath){
        this.filePath = filePath;
        registeredUsersFile= new File(filePath);
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
        String data = clientLoginInfo.getNickname() + " " + clientLoginInfo.getSecuredPassword().password + " " + clientLoginInfo.getSecuredPassword().salt;
        overrideDB(data);
    }

    private boolean clientExistInDB(String nickname) {
        try {
            scanner = new Scanner(registeredUsersFile);
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
            scanner = new Scanner(registeredUsersFile);
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
            FileWriter fileWriter = new FileWriter(registeredUsersFile.getAbsoluteFile(), true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
