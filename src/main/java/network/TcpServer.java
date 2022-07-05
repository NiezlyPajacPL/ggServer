package network;

import helpers.*;
import managers.ConnectionData;
import managers.SubtitlesPrinter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TcpServer implements Server {
    ServerSocket serverSocket;
    int port;
    SubtitlesPrinter subtitlesPrinter;
    Map<String, ConnectionData> users = new HashMap<>();
    MessageHelper messageHelper = new MessageHelper(users);

    public TcpServer(int port,SubtitlesPrinter subtitlesPrinter){
        this.port = port;
        this.subtitlesPrinter = subtitlesPrinter;
    }

    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true){
                Socket socket = serverSocket.accept();
                ClientSocket clientSocket = new ClientSocket(socket, users,subtitlesPrinter,messageHelper);
                Thread clientThread = new Thread(clientSocket);
                clientThread.start();
           /*
                String clientName = clientSocket.getClientName();
                threadMap.put(clientName,new ConnectionData(socket.getInputStream(),socket.getOutputStream()));
                System.out.println("registered client: " + clientName);
            */
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void sendPacket(Packet packetToSend) {

    }

    @Override
    public Packet receivePacket() {
        return null;
    }

}
