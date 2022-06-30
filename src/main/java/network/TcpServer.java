package network;

import helpers.Packet;
import managers.SubtitlesPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer implements Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;
    private SubtitlesPrinter subtitlesPrinter;
    private int port;

    public TcpServer(SubtitlesPrinter subtitlesPrinter,int port) throws IOException {
     this.subtitlesPrinter = subtitlesPrinter;
     this.port = port;
     serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Client connected");
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true){
                String message = input.readLine();
                System.out.println(message);
                output.println(message);
                if(input.readLine().equals("asd")){
                    output.println("AHA");
                }else if(input.readLine().contains("/msg")){
                    output.println(clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " said: " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            }
            else {
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

    @Override
    public void sendPacket(Packet packetToSend) {

    }

    @Override
    public DatagramPacket receivePacket() {
        return null;
    }

}

