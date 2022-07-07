package network;

import helpers.MessageHelper;
import helpers.Packet;
import managers.ConnectionData;
import managers.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class UdpServer implements Server {
    private DatagramSocket socket;
    Logger logger;
    Map<String, ConnectionData> users = new HashMap<>();
    MessageHelper messageHelper = new MessageHelper();

    public UdpServer(int port, Logger logger) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.logger = logger;
    }

 //   PasswordHasher passwordHasher = new PasswordHasher();

    public void run() {
    /*    while (true) {
            CommandMapperImpl commandMapper = new CommandMapperImpl();
            CommandHandler commandHandler = new CommandHandler(logger, messageHelper, users);
            MessageType messageType;
            Packet receivedPacket = receivePacket();
            messageType = commandMapper.mapCommand(receivedPacket);

            if (messageType instanceof Registration) {
                commandHandler.registerUser((Registration) messageType,receivedPacket.getConnectionData());
                sendPacket(commandHandler.registerUser((Registration) messageType),receivedPacket.getConnectionData());
            } else if (messageType instanceof UsersListSender) {
                sendPacket(commandHandler.sendUsersList((UsersListSender) messageType));
            } else if (messageType instanceof Messenger) {
                sendPacket(commandHandler.sendMessage((Messenger) messageType));
            }else if (messageType instanceof Login) {
                sendPacket(commandHandler.loginUser((Login) messageType));
            } else if (messageType instanceof Logout) {
                sendPacket(commandHandler.logoutUser((Logout) messageType));
            }
        }
     */
    }

    @Override
    public void sendPacket(Packet packetToSendInformation) {
        DatagramPacket packetToSend = new DatagramPacket(packetToSendInformation.getData(),
                packetToSendInformation.getData().length,
                packetToSendInformation.getConnectionData().getInetAddress(),
                packetToSendInformation.getConnectionData().getPort()
        );
        try {
            socket.send(packetToSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Packet receivePacket() {
        byte[] bufToReceive = new byte[256];
        DatagramPacket receivedDatagramPacket = new DatagramPacket(bufToReceive, bufToReceive.length);
        try {
            socket.receive(receivedDatagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Packet(receivedDatagramPacket.getData(),new ConnectionData(receivedDatagramPacket.getAddress(), receivedDatagramPacket.getPort()));
    }

}

