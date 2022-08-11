import helpers.Logger;
import managers.DataBase;
import managers.DataBaseImpl;
import managers.PasswordHasher;
import network.TcpServer;

import java.net.SocketException;

public class Main {
    public static void main(String[] args) throws SocketException {
        DataBase dataBase = new DataBaseImpl("src/main/resources/dataBase/RegisteredClients.txt");
        PasswordHasher passwordHasher = new PasswordHasher(dataBase);

        TcpServer server = new TcpServer(4445, dataBase, passwordHasher);
        Thread thread = new Thread(server);
        thread.start();
        Logger.printLogServerStarted();
    }
}
