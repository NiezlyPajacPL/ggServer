import helpers.Logger;
import managers.*;
import network.TcpServer;

public class Main {
    private static final String sqliteDbUrl = "jdbc:sqlite:src/main/resources/dataBase/dataBase.db";
    private static final int port = 4445;

    public static void main(String[] args) {
        DataBase sqliteDB = new DataBaseImpl(sqliteDbUrl);
        PasswordHandler passwordHandler = new PasswordHandler(nickname ->sqliteDB.getClient(nickname).getSecuredPassword());

        TcpServer server = new TcpServer(port,sqliteDB,passwordHandler);
        Thread thread = new Thread(server);
        thread.start();
        Logger.printLogServerStarted();
    }
}
