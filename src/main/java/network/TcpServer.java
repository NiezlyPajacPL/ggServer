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
    InputHelper inputHelper;
    Map<String, ConnectionData> threadMap = new HashMap<>();

    public TcpServer(int port,SubtitlesPrinter subtitlesPrinter,InputHelper inputHelper){
        this.port = port;
        this.subtitlesPrinter = subtitlesPrinter;
        this.inputHelper = inputHelper;
    }

    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true){
                Socket socket = serverSocket.accept();
                ClientSocket clientSocket = new ClientSocket(socket,threadMap,subtitlesPrinter,inputHelper);
                clientSocket.start();
                String clientName = clientSocket.getClientName();
                threadMap.put(clientName,new ConnectionData(socket.getInputStream(),socket.getOutputStream()));
                System.out.println("registered client: " + clientName);
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
