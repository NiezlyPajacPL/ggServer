package network;

import helpers.*;
import managers.DataBase;
import managers.PasswordHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TcpServer implements Runnable {
    private final int port;
    private final Map<String, Socket> onlineUsers = new HashMap<>();
    private final DataBase dB;
    private final PasswordHandler passwordHandler;


    public TcpServer(int port, DataBase dB, PasswordHandler passwordHandler) {
        this.port = port;
        this.dB = dB;
        this.passwordHandler = passwordHandler;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                MessageListener messageListener = new MessageListener() {
                    @Override
                    public Socket onMessageReceivedGetUser(String receiver) {
                        return onlineUsers.get(receiver);
                    }

                    @Override
                    public void onClientLoggingIn(String nickname) {
                        onlineUsers.put(nickname, socket);
                    }

                    @Override
                    public void onClientLoggedOut(String nickname) {
                        onlineUsers.remove(nickname);
                    }

                    @Override
                    public Map<String, Socket> getUsersList() {
                        return onlineUsers;
                    }
                };
                ClientSocket clientSocket = new ClientSocket(socket, messageListener, dB, passwordHandler);
                Thread clientThread = new Thread(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
