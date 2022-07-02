package network;

import helpers.Packet;
import managers.SubtitlesPrinter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TcpServerConnector implements Runnable{
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private SubtitlesPrinter subtitlesPrinter;
    private int port;
    Map<String, PrintWriter> tcpUsers = new HashMap<>();
    Socket socket;

    public TcpServerConnector(SubtitlesPrinter subtitlesPrinter, int port) throws IOException {
        this.subtitlesPrinter = subtitlesPrinter;
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (true) {
                socket = serverSocket.accept();
                Thread thread = new Thread(new TcpServer(socket,subtitlesPrinter,tcpUsers));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*
    private String receiveMessage() throws IOException {
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message = input.readLine();
        System.out.println(message);
        //     output.println(message);
        return message;
    }

    private void sendMsg(String message) throws IOException {
        output = new PrintWriter(clientSocket.getOutputStream(), true);
        output.println(message);//
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            System.out.println("Client connected");
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String greeting = input.readLine();
            if ("hello server".equals(greeting)) {
                output.println("hello client");
            } else {
                output.println("unrecognised greeting");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendMessage(String msg) {
        String resp = null;
        try {
            output.println(msg);
            resp = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    public void stopConnection() {
        try {
            input.close();
            output.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}

