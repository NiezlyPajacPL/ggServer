import helpers.InputHelper;
import managers.SubtitlesPrinter;
import network.TcpServer;
import network.TcpServerThread;
import network.UdpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        UdpServer server;
        SubtitlesPrinter subtitlesPrinter = new SubtitlesPrinter();
        ArrayList<TcpServer> threadArrayList = new ArrayList<>();
        Map<String,TcpServer> threadMap = new HashMap<>();
        InputHelper inputHelper = new InputHelper();
        {
            subtitlesPrinter.printLogServerStarted();
            try(ServerSocket serverSocket = new ServerSocket(5000)) {
                while (true){
                    Socket socket = serverSocket.accept();
                    TcpServer serverThread = new TcpServer(socket,threadMap,subtitlesPrinter,inputHelper);
                    serverThread.start();
                    String clientName = serverThread.getClientName();
                    threadMap.put(clientName,serverThread);
                    System.out.println("registered client: " + clientName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
             /*   TcpServer tcpServerConnector = new TcpServer(subtitlesPrinter, 5000);
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
