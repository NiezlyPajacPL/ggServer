import helpers.InputHelper;
import managers.SubtitlesPrinter;
import network.ClientSocket;
import network.TcpServer;
import network.UdpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        UdpServer server;
        SubtitlesPrinter subtitlesPrinter = new SubtitlesPrinter();
        Map<String, ClientSocket> threadMap = new HashMap<>();
        InputHelper inputHelper = new InputHelper();
        {
            TcpServer tcpServer = new TcpServer(5000,subtitlesPrinter,inputHelper);
            Thread thread = new Thread(tcpServer);
            thread.start();
            subtitlesPrinter.printLogServerStarted();
             /*   ClientSocket tcpServerConnector = new ClientSocket(subtitlesPrinter, 5000);
                subtitlesPrinter.printLogServerStarted();
                Thread thread = new Thread(tcpServerConnector);
                thread.start();
               */
                /*
                server = new UdpServer(subtitlesPrinter, 4445);
                subtitlesPrinter.printLogServerStarted();
                Thread thread = new Thread(server);
                thread.start();
*/
        }
    }
}
