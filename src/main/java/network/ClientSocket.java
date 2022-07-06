package network;

import helpers.MessageHelper;
import helpers.Packet;
import managers.ConnectionData;
import managers.SubtitlesPrinter;
import managers.commands.CommandMapperImpl;
import managers.commands.Commands;
import managers.commands.messageTypes.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ClientSocket implements Server {
    private SubtitlesPrinter subtitlesPrinter;
    private Map<String, ConnectionData> users;
    private Socket socket;
    private PrintWriter messageSender;
    private MessageHelper messageHelper;


    public ClientSocket(Socket socket, Map<String, ConnectionData> users, SubtitlesPrinter subtitlesPrinter,MessageHelper messageHelper) throws IOException {
        this.users = users;
        this.socket = socket;
        this.subtitlesPrinter = subtitlesPrinter;
        this.messageHelper = messageHelper;
    }

    @Override
    public void run() {

        while (true) {
            CommandMapperImpl commandMapper = new CommandMapperImpl();
            Commands commands = new Commands(subtitlesPrinter, messageHelper, users);
            MessageType messageType;
            Packet receivedPacket = receivePacket();
            messageType = commandMapper.mapCommand(receivedPacket);

            if (messageType instanceof Registration) {
                sendPacket(commands.registerUser((Registration) messageType));
            } else if (messageType instanceof UsersListSender) {
                sendPacket(commands.sendUsersList((UsersListSender) messageType));
            } else if (messageType instanceof Messenger) {
                sendPacket(commands.sendMessage((Messenger) messageType));
            }else if (messageType instanceof Login) {
               sendPacket(commands.loginUser((Login) messageType));
            } else if (messageType instanceof Logout) {
               sendPacket(commands.logoutUser((Logout) messageType));
               break;
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
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

}


