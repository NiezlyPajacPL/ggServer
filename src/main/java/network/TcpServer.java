package network;

import helpers.InputHelper;
import helpers.Packet;
import managers.ConnectionData;
import managers.SubtitlesPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TcpServer extends Thread {
    private SubtitlesPrinter subtitlesPrinter;
    Map<String,TcpServer> threadMap;
    Map<ConnectionData, PrintWriter> tcpUsers = new HashMap<>();
    Socket socket;
    private PrintWriter output;
    InputHelper inputHelper;

    public TcpServer(Socket socket, Map<String,TcpServer> threadMap, SubtitlesPrinter subtitlesPrinter, InputHelper inputHelper) throws IOException {
        this.threadMap = threadMap;
        this.socket = socket;
        this.subtitlesPrinter = subtitlesPrinter;
        this.inputHelper = inputHelper;
    }

    @Override
    public void run() {
        try {
            //reading the input from client
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //returning output to the client
            output = new PrintWriter(socket.getOutputStream(),true);

            while(true){
                String inputString = input.readLine();
                if (inputString.contains("/register")) {
                    String sender = inputHelper.defineSecondWord(inputString);
               //     threadMap.put(sender,tcpServer);
                }else if(inputString.contains("/msg")){
                    String receiver = inputHelper.defineSecondWord(inputString);
                    String message = inputHelper.defineMessageFromInput(inputString);

                    output = new PrintWriter(threadMap.get(receiver).socket.getOutputStream(),true);
                    output.println(message);
                } else if(inputString.equals("/exit")){
                    break;
                }
             //   printToAllClients(outputString);
                System.out.println("Server received " + inputString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getClientName() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputString = input.readLine();
            return  inputHelper.defineSecondWord(inputString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "asd";
    }
}
    /*
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

*/
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


