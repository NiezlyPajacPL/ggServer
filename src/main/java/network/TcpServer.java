package network;

import helpers.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TcpServer implements Runnable {
    int port;
    Map<String, Socket> users = new HashMap<String, Socket>();
    MessageHelper messageHelper = new MessageHelper();

    public TcpServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
          //      serverSocket.setSoTimeout(20000);
                MessageListener messageListener = new MessageListener() {
                    @Override
                    public Socket onMessageReceivedGetReceiverSocket(String receiver) {
                        return users.get(receiver);
                    }

                    @Override
                    public void onClientLoggingIn(String nickname) {
                        users.put(nickname, socket);
                    }

                    @Override
                    public void onClientLoggedOut(String nickname) {
                        users.remove(nickname);
                    }

                    @Override
                    public String onUsersListRequest() {
                        return users.keySet().toString();
                    }
                };
                ClientSocket clientSocket = new ClientSocket(socket, messageHelper,messageListener);
                Thread clientThread = new Thread(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
