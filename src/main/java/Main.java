import helpers.Logger;
import managers.DataBase;
import managers.DataBaseImpl;
import managers.PasswordHasher;
import network.ClientSocket;
import network.TcpServer;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SocketException {
        Map<String, ClientSocket> threadMap = new HashMap<>();
        DataBase dataBase = new DataBaseImpl("src/main/java/managers/commands/RegisteredClients.txt");
        PasswordHasher passwordHasher = new PasswordHasher(dataBase);

        TcpServer server = new TcpServer(4445, dataBase, passwordHasher);
        Thread thread = new Thread(server);
        thread.start();
        Logger.printLogServerStarted();
    }
}
