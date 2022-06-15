import managers.SubtitlesPrinter;
import network.UdpServer;

import java.net.SocketException;

public class Main {
    public static void main(String[] args) {
        UdpServer server;
        SubtitlesPrinter subtitlesPrinter = new SubtitlesPrinter();
        {
            try {
                server = new UdpServer(subtitlesPrinter,4445);
                subtitlesPrinter.printLogServerStarted();
                Thread thread = new Thread(server);
                thread.start();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }
}
