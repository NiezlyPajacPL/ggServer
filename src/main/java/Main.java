import managers.ConnectionData;
import managers.SubtitlesPrinter;
import network.Server;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Server server;
        SubtitlesPrinter subtitlesPrinter = new SubtitlesPrinter();
        {
            try {

                server = new Server(subtitlesPrinter);
                subtitlesPrinter.printLogServerStarted();
                server.start();


            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }
}
