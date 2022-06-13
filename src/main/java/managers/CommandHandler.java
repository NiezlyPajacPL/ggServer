package managers;

import helpers.InputHandler;
import helpers.PacketInformation;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CommandHandler {
    private boolean running;
    private final byte[] bufToReceive = new byte[256];
    private byte[] bufToSend;
    private final ArrayList<String> clientList = new ArrayList<>();
    Map<String, ConnectionData> clients = new HashMap<>();
    InputHandler inputHandler = new InputHandler();
    InetAddress inetAddress;
    int port;
    SubtitlesPrinter subtitlesPrinter;
    String receiver;

    public CommandHandler(SubtitlesPrinter subtitlesPrinter) {
        this.subtitlesPrinter = subtitlesPrinter;
    }


    public PacketInformation commands(DatagramPacket receivedPacket) {
        String input = new String(receivedPacket.getData());
        input.replaceAll("[\\s\u0000]+", "");
        if (input.contains("/register")) {
            String nickname = inputHandler.defineWhoWantsToRegister(input).replaceAll("[\\s\u0000]+", "");
            if (!checkIfNicknameIsAlreadyUsed(nickname)) {
                overrideAddresses(receivedPacket.getAddress(), receivedPacket.getPort());
                addClientToDataBase(nickname);
                stringToSendHandler("Registered Successfully", receivedPacket,false);
                subtitlesPrinter.printLogClientRegistered(nickname, inetAddress, port);
                return new PacketInformation(bufToSend, new ConnectionData(inetAddress, port));
            } else {
                stringToSendHandler("Nickname is already in use.", receivedPacket,false);
                return new PacketInformation(bufToSend, new ConnectionData(inetAddress, port));
            }

        } else if (input.contains("/allUsers") || input.contains("/allusers") || input.contains("/users")) {
            subtitlesPrinter.printLogUsersListRequest();
            overrideAddresses(receivedPacket.getAddress(), receivedPacket.getPort());

            stringToSendHandler(clientList.toString(),receivedPacket,false);
            return new PacketInformation(bufToSend, new ConnectionData(inetAddress, port));
        } else if (input.contains("/msg")) {
            subtitlesPrinter.printLogSomeoneTriedToSendMessage();
            receiver = inputHandler.defineReceiver(input).replaceAll("[\\s\u0000]+", "");

            if (checkIfReceiverIsOnTheList(receiver.toLowerCase(Locale.ROOT))) {
                ConnectionData senderConnectionData = new ConnectionData(receivedPacket.getAddress(),receivedPacket.getPort());
                String sender = getSender(senderConnectionData);
                stringToSendHandler(InputHandler.defineMessageFromInput(input), receivedPacket,true);
                ConnectionData receiverData = clients.get(receiver);

                subtitlesPrinter.printLogSuccessfullySentMessage(sender,receiver);
                return new PacketInformation(bufToSend, receiverData);
            } else {
                subtitlesPrinter.printLogMessageWasNotSent();
            }
        } else if (receiver != null) {
            subtitlesPrinter.printLogSomeoneTriedToSendMessage();
            if (checkIfReceiverIsOnTheList(receiver.toLowerCase(Locale.ROOT))) {
                ConnectionData senderConnectionData = new ConnectionData(receivedPacket.getAddress(),receivedPacket.getPort());
                String sender = getSender(senderConnectionData);

                stringToSendHandler(input, receivedPacket,true);
                ConnectionData receiverData = clients.get(receiver);
                subtitlesPrinter.printLogSuccessfullySentMessage(sender,receiver);
                return new PacketInformation(bufToSend, receiverData);
            } else {
                subtitlesPrinter.printLogMessageWasNotSent();
            }
        }
        overrideAddresses(receivedPacket.getAddress(), receivedPacket.getPort());
        stringToSendHandler("Something went wrong.", receivedPacket, false);
        return new PacketInformation(bufToSend, new ConnectionData(inetAddress, port));
    }


    private void addClientToDataBase(String nickname) {
        clients.put(nickname, new ConnectionData(inetAddress, port));
        clientList.add(nickname);
        clients.put(nickname, new ConnectionData(inetAddress, port));
    }

    private String getSender(ConnectionData senderConnectionData) {
        for (Map.Entry<String, ConnectionData> entry : clients.entrySet()) {
            if (entry.getValue() == senderConnectionData) {
                return entry.getKey();
            }
        }
        return "UNKNOWN";
    }

    /*   private String getSender(InetAddress senderAddress, int senderPort){

           for(int i = 0; i < list.size(); i++){
               if(senderAddress == list.get(i).connectionData.inetAddress){
                   if(senderPort == list.get(i).connectionData.port)
                       return list.get(i).key;
               }
           }

           return null;
       }
   */
    private void stringToSendHandler(String text, DatagramPacket receivedPacket, boolean senderRequired) {
        if (senderRequired) {
            String sender = getSender(new ConnectionData(receivedPacket.getAddress(),receivedPacket.getPort()));
            String textToSend = sender + ": " + text;
            bufToSend = textToSend.getBytes(StandardCharsets.UTF_8);
        } else {
            bufToSend = text.getBytes(StandardCharsets.UTF_8);
        }
    }

    private void overrideAddresses(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    private boolean checkIfNicknameIsAlreadyUsed(String nickname) {
        for (int i = 0; i < clientList.size(); i++) {
            if (Objects.equals(clientList.get(i), nickname)) {
                return true;
            }
        }
        return false;
    }


    private boolean checkIfReceiverIsOnTheList(String receiver) {
        for (int i = 0; i < clientList.size(); i++) {
            if (clients.containsKey(receiver)) {
                return true;
            }
        }
        return false;
    }
}