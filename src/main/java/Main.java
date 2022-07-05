import helpers.InputHelper;
import managers.SubtitlesPrinter;
import network.ClientSocket;
import network.Server;
import network.TcpServer;
import network.UdpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SocketException {
        SubtitlesPrinter subtitlesPrinter = new SubtitlesPrinter();
        Map<String, ClientSocket> threadMap = new HashMap<>();
        {
            //To change network protocol,
            // just change the Server implementation to TcpServer / UdpServer

            Server server = new TcpServer(4445,subtitlesPrinter);
            Thread thread = new Thread(server);
            thread.start();
            subtitlesPrinter.printLogServerStarted();
        }
    }
}
