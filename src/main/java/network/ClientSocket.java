package network;

import helpers.InputHelper;
import managers.ConnectionData;
import managers.SubtitlesPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientSocket extends Thread {
    private SubtitlesPrinter subtitlesPrinter;
    Map<String, ClientSocket> threadMap;
    Map<ConnectionData, PrintWriter> tcpUsers = new HashMap<>();
    Socket socket;
    private PrintWriter messageSender;
    InputHelper inputHelper;

    public ClientSocket(Socket socket, Map<String, ClientSocket> threadMap, SubtitlesPrinter subtitlesPrinter, InputHelper inputHelper) throws IOException {
        this.threadMap = threadMap;
        this.socket = socket;
        this.subtitlesPrinter = subtitlesPrinter;
        this.inputHelper = inputHelper;
    }

    @Override
    public void run() {
        try {
            //reading the input from client
            BufferedReader receivedBufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while(true){
                String receivedString = receivedBufferReader.readLine();

               if(receivedString.contains("/msg")){
                    String receiver = inputHelper.getFirstArgument(receivedString);
                    String message = inputHelper.defineMessageFromInput(receivedString);

                    messageSender = new PrintWriter(threadMap.get(receiver).socket.getOutputStream(),true);
                    messageSender.println(message);
                } else if(receivedString.equals("/exit")){
                    break;
                }
             //   printToAllClients(outputString);
                System.out.println("Server received " + receivedString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getClientName() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputString = input.readLine();
            return  inputHelper.getFirstArgument(inputString);
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
                Thread thread = new Thread(new ClientSocket(socket,subtitlesPrinter,tcpUsers));
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


