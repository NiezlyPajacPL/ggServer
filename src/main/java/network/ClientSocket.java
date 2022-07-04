package network;

import helpers.DataBaseManager;
import helpers.InputHelper;
import helpers.MessageHelper;
import helpers.Packet;
import managers.ConnectionData;
import managers.SubtitlesPrinter;
import managers.commands.CommandMapperImpl;
import managers.commands.messageTypes.MessageType;
import managers.commands.messageTypes.Messenger;
import managers.commands.messageTypes.Registration;
import managers.commands.messageTypes.UsersListSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ClientSocket implements Server {
    private SubtitlesPrinter subtitlesPrinter;
    Map<String, ConnectionData> users;
    Map<ConnectionData, PrintWriter> tcpUsers = new HashMap<>();
    Socket socket;
    private PrintWriter messageSender;
    InputHelper inputHelper;

    DataBaseManager dataBaseManager;
    {
        try {
            dataBaseManager = new DataBaseManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ClientSocket(Socket socket, Map<String, ConnectionData> users, SubtitlesPrinter subtitlesPrinter, InputHelper inputHelper) throws IOException {
        this.users = users;
        this.socket = socket;
        this.subtitlesPrinter = subtitlesPrinter;
        this.inputHelper = inputHelper;
    }

    @Override
    public void run() {
        //reading the input from client


        while (true) {
            CommandMapperImpl commandMapper = new CommandMapperImpl(users);
            MessageType messageType;
            Packet receivedPacket = receivePacket();
            messageType = commandMapper.mapCommand(receivedPacket);

            if (messageType instanceof Registration) {
                registerUser((Registration) messageType);
            } else if (messageType instanceof UsersListSender) {
                sendUsersList((UsersListSender) messageType);
            } else if (messageType instanceof Messenger) {
                sendMessage((Messenger) messageType);
            }

   /*        if(receivedString.contains("/msg")){
                String receiver = inputHelper.getFirstArgument(receivedString);
                String message = inputHelper.defineMessageFromInput(receivedString);

                messageSender = new PrintWriter(users.get(receiver).getSendingStream(),true);
                messageSender.println(message);
            } else if(receivedString.equals("/exit")){
                break;
            }
         //   printToAllClients(outputString);
            System.out.println("Server received " + receivedString);

    */
        }

    }

    @Override
    public void sendPacket(Packet packetToSend) {
        messageSender = new PrintWriter(packetToSend.getConnectionData().getSendingStream(), true);
        messageSender.println(new String(packetToSend.getData()));
    }

    @Override
    public Packet receivePacket() {
        try {
            BufferedReader receivedBufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String receivedString = receivedBufferReader.readLine();

            return new Packet(receivedString.getBytes(StandardCharsets.UTF_8), new ConnectionData(socket.getInputStream(), socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void registerUser(Registration registration) {
        users.put(registration.name, registration.connectionData);
        MessageHelper messageHelper = new MessageHelper(users);
        subtitlesPrinter.printLogGeneratedPassword();
        subtitlesPrinter.printLogClientRegistered(registration.name, registration.connectionData);

        Packet packetToSend = new Packet(messageHelper.registeredSuccessfully.getBytes(StandardCharsets.UTF_8), registration.connectionData);
        sendPacket(packetToSend);
    }

    private void sendUsersList(UsersListSender usersListSender) {
        MessageHelper messageHelper = new MessageHelper(users);
        subtitlesPrinter.printLogUsersListRequest();

        Packet packetToSend = new Packet(messageHelper.clientList().getBytes(StandardCharsets.UTF_8), usersListSender.connectionData);
        sendPacket(packetToSend);
    }

    private void sendMessage(Messenger messenger) {
        byte[] messageToSend = stringToSendHelper(messenger.message, messenger.sender);
        subtitlesPrinter.printLogSuccessfullySentMessage(messenger.sender, messenger.receiver, messenger.message);
        Packet packetToSend = new Packet(messageToSend,messenger.connectionData);
        sendPacket(packetToSend);
    }

    private byte[] stringToSendHelper(String text, String sender) {
        String textToSend = sender + ": " + text;
        return textToSend.getBytes(StandardCharsets.UTF_8);
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


