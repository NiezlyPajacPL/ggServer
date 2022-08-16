import helpers.Logger;
import helpers.SecuredPassword;
import managers.*;
import network.TcpServer;

import java.net.SocketException;

public class Main {
    private static final String dataBasePath = "src/main/resources/dataBase/RegisteredClients.txt";
    private static final String sqliteDbPath = "jdbc:sqlite:src/main/resources/dataBase/dataBase.db";

    public static void main(String[] args) throws SocketException {
        DataBase dataBase = new DataBaseImpl(dataBasePath);
        DataBase sqliteDB = new SqliteDB(sqliteDbPath);
        PasswordHandler passwordHandler = new PasswordHandler(nickname -> dataBase.getClient(nickname).getSecuredPassword());
        int port = 4445;

        TcpServer server = new TcpServer(port, dataBase, passwordHandler);
        Thread thread = new Thread(server);
        thread.start();
        Logger.printLogServerStarted();
    }
}
