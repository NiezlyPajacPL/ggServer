package network;

import helpers.*;
import managers.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TcpServer implements Server {
    int port;
    Map<String, Socket> users = new HashMap<String, Socket>();
    MessageHelper messageHelper = new MessageHelper();

    public TcpServer(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true){
                Socket socket = serverSocket.accept();
                UserRegistrationListener userRegistrationListener = new UserRegistrationListener() {
                    @Override
                    public void onClientRegistered(String nickname) {
                        users.put(nickname,socket);
                    }
                };
                ClientSocket clientSocket = new ClientSocket(socket,messageHelper,userRegistrationListener);
                Thread clientThread = new Thread(clientSocket);
                clientThread.start();
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
