import managers.SubtitlesPrinter;
import network.TcpServer;
import network.UdpServer;

import java.net.SocketException;

public class Main {
    public static void main(String[] args) {
        UdpServer server;
        SubtitlesPrinter subtitlesPrinter = new SubtitlesPrinter();
        {
            try {
                TcpServer tcpServer = new TcpServer();
                tcpServer.start(6666);

                server = new UdpServer(subtitlesPrinter, 4445);
                subtitlesPrinter.printLogServerStarted();
                Thread thread = new Thread(server);
                thread.start();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }
}
