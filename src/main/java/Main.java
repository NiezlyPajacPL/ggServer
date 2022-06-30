import helpers.FileHandler;
import managers.ConnectionData;
import managers.SubtitlesPrinter;
import network.TcpServer;
import network.UdpServer;

import java.io.IOException;
import java.net.SocketException;

public class Main {
    public static void main(String[] args) {
        UdpServer server;
        SubtitlesPrinter subtitlesPrinter = new SubtitlesPrinter();
        {


            try {
                TcpServer tcpServer = new TcpServer(subtitlesPrinter, 5000);
                subtitlesPrinter.printLogServerStarted();
                Thread thread = new Thread(tcpServer);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*    server = new UdpServer(subtitlesPrinter, 4445);
                subtitlesPrinter.printLogServerStarted();
                Thread thread = new Thread(server);
                thread.start();

             */
        }
    }
}
