package network;

import helpers.*;
import helpers.Listeners.MessageListener;
import helpers.Listeners.UserRegistrationListener;
import managers.ConnectionData;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
                UserRegistrationListener userRegistrationListener = new UserRegistrationListener() {
                    @Override
                    public void onClientRegistered(String nickname) {
                        users.put(nickname, socket);
                    }
                };
                MessageListener messageListener = new MessageListener() {
                    @Override
                    public String onMessageReceivedGetSender(ConnectionData senderConnectionData) {
                        return getSender(senderConnectionData);
                    }

                    @Override
                    public Socket onMessageReceivedGetReceiverSocket(String receiver) {
                        return users.get(receiver);
                    }
                };
                ClientSocket clientSocket = new ClientSocket(socket, messageHelper, userRegistrationListener,messageListener);
                Thread clientThread = new Thread(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSender(ConnectionData senderConnectionData) {
        for (Map.Entry<String, Socket> entry : users.entrySet()) {
            if (senderConnectionData.getInetAddress() != null) {
                if ((Objects.equals(entry.getValue().getInetAddress(), senderConnectionData.getInetAddress())) && Objects.equals(entry.getValue().getPort(), senderConnectionData.getPort())) {
                    return entry.getKey();
                }
            } else {
                try {
                    if ((Objects.equals(entry.getValue().getOutputStream(), senderConnectionData.getSendingStream()))) {
                        return entry.getKey();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "UNKNOWN";
    }
}
