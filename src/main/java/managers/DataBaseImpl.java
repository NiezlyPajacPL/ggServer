package managers;

import helpers.ClientLoginInfo;
import helpers.InputHelper;
import helpers.SecuredPassword;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class DataBaseImpl implements DataBase {
    private final File registeredUsersFile;
    private Scanner scanner;
    private final InputHelper inputHelper = new InputHelper();

    public DataBaseImpl(String filePath) {
        registeredUsersFile = new File(filePath);
    }

    @Override
    public ClientLoginInfo getClient(String nickname) {
        if (clientExistInDB(nickname)) {
            return new ClientLoginInfo(nickname, getHashedPassword(nickname));
        } else {
            return null;
        }
    }

    @Override
    public void saveClient(ClientLoginInfo clientLoginInfo) {
        String data = clientLoginInfo.getNickname() + " " + clientLoginInfo.getSecuredPassword().getPassword() + " " + clientLoginInfo.getSecuredPassword().getSalt();
        createChatHistoryFolder(clientLoginInfo.getNickname());
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
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createChatHistoryFolder(String nickname){
        String filePath = "C:\\Users\\Dawid\\IdeaProjects\\ggserver\\src\\main\\resources\\dataBase\\chatHistory" + "/" + nickname;
        System.out.println("EO E");
        File chatHistory = new File(filePath);
    //    chatHistory.mkdirs();
        if (!chatHistory.exists()) {
            if(chatHistory.mkdirs()){
                System.out.println("Created chat history folder for " + nickname);
            }
        }
    }

}
