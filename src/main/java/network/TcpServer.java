package network;

import helpers.*;
import managers.DataBase;
import managers.PasswordHasher;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TcpServer implements Runnable {
    private final int port;
    private final Map<String, Socket> users = new HashMap<>();
    private final DataBase dataBase;
    private final PasswordHasher passwordHasher;


    public TcpServer(int port, DataBase dataBase, PasswordHasher passwordHasher) {
        this.port = port;
        this.dataBase = dataBase;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                MessageListener messageListener = new MessageListener() {
                    @Override
                    public Socket onMessageReceivedGetUser(String receiver) {
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
                    public Map<String, Socket> getUsersList() {
                        return users;
                    }
                };
                ClientSocket clientSocket = new ClientSocket(socket, messageListener,dataBase,passwordHasher);
                Thread clientThread = new Thread(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
