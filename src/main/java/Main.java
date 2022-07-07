import managers.Logger;
import network.ClientSocket;
import network.Server;
import network.TcpServer;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SocketException {
        Map<String, ClientSocket> threadMap = new HashMap<>();
        {
            //To change network protocol,
            // just change the Server implementation to TcpServer / UdpServer

            Server server = new TcpServer(4445);
            Thread thread = new Thread(server);
            thread.start();
            Logger.printLogServerStarted();
        }
    }
}
