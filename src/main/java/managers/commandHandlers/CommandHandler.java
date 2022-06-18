package managers.commandHandlers;

import helpers.CommandData;
import helpers.FileHandler;
import helpers.InputHelper;
import helpers.Packet;
import managers.ConnectionData;
import managers.SubtitlesPrinter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CommandHandler {
    private byte[] bufToSend;
    InputHelper inputHandler = new InputHelper();
    InetAddress inetAddress;
    int port;
    FileHandler fileHandler;
    private String receiver;

    {
        try {
            fileHandler = new FileHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CommandData commands(DatagramPacket receivedPacket) {
        String input = new String(receivedPacket.getData());

        if (input.contains("/register")) {
            return new CommandData("registration", receivedPacket);

        } else if (input.contains("/allUsers") || input.contains("/allusers") || input.contains("/users")) {

            return new CommandData("usersRequest", receivedPacket);
        }
        return new CommandData("UNKNOWN", receivedPacket);
    }
}
/*

    private void addClientToDataBase(String nickname,String password) {
        clients.put(nickname, new ConnectionData(inetAddress, port));
        clientList.add(nickname);
        try {
            FileHandler fileHandler = new FileHandler();
            fileHandler.overrideDataBase(nickname + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateDataBase(String nickname){
        clients.put(nickname, new ConnectionData(inetAddress,port));
        clientList.add(nickname);
    }

    private String getSender(ConnectionData senderConnectionData) {
        for (Map.Entry<String, ConnectionData> entry : clients.entrySet()) {
            if ((Objects.equals(entry.getValue().getInetAddress(), senderConnectionData.getInetAddress())) && Objects.equals(entry.getValue().getPort(), senderConnectionData.getPort())) {
                return entry.getKey();
            }
        }
        return "UNKNOWN";
    }

    private void stringToSendHandler(String text, DatagramPacket receivedPacket, boolean senderRequired) {
        if (senderRequired) {
            String sender = getSender(new ConnectionData(receivedPacket.getAddress(), receivedPacket.getPort()));
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

    private boolean isUserRegistered(String nickname) {
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
  /*  public Packet commands(DatagramPacket receivedPacket) {
        String input = new String(receivedPacket.getData());

        if (input.contains("/register")) {
            String nickname = inputHandler.defineWhoWantsToRegister(input).replaceAll("[\\s\u0000]+", "").toLowerCase(Locale.ROOT);
            String password = inputHandler.definePasswordFromInput(input).replaceAll("[\\s\u0000]+", "");
            if (!isUserRegistered(nickname)) {
                overrideAddresses(receivedPacket.getAddress(), receivedPacket.getPort());
                addClientToDataBase(nickname,password);

                stringToSendHandler("Registered Successfully", receivedPacket, false);
                subtitlesPrinter.printLogClientRegistered(nickname, inetAddress, port);
            } else {
                stringToSendHandler("Nickname is already in use.", receivedPacket, false);
                System.out.println("Failed registration.");
            }
            return new Packet(bufToSend, new ConnectionData(inetAddress, port));



        } else if(input.contains("/login")){
            String nickname = inputHandler.defineWhoWantsToRegister(input).replaceAll("[\\s\u0000]+", "").toLowerCase(Locale.ROOT);
            String password = inputHandler.dataBaseDefinePassword(input).replaceAll("[\\s\u0000]+", "");

            if(fileHandler.doesClientExistInDataBase(nickname)){
                if(fileHandler.doesPasswordMatch(password)){
                    overrideAddresses(receivedPacket.getAddress(), receivedPacket.getPort());
                    updateDataBase(nickname);
                    stringToSendHandler("Hello again " + nickname + "!", receivedPacket, false);
                    subtitlesPrinter.printLogClientLoggedIn(nickname);

                }else{
                    stringToSendHandler("Password does not match.", receivedPacket, false);
                }
            }else{
                stringToSendHandler("Client does not exist in data base.", receivedPacket, false);
            }
            return new Packet(bufToSend, new ConnectionData(inetAddress, port));
        }

        else if (input.contains("/allUsers") || input.contains("/allusers") || input.contains("/users")) {
            subtitlesPrinter.printLogUsersListRequest();
            overrideAddresses(receivedPacket.getAddress(), receivedPacket.getPort());

            stringToSendHandler(clientList.toString(), receivedPacket, false);
            return new Packet(bufToSend, new ConnectionData(inetAddress, port));
        } else if (input.contains("/msg")) {
            subtitlesPrinter.printLogSomeoneTriedToSendMessage();
            receiver = inputHandler.defineReceiver(input).replaceAll("[\\s\u0000]+", "").toLowerCase(Locale.ROOT);

            if (checkIfReceiverIsOnTheList(receiver.toLowerCase(Locale.ROOT))) {
                ConnectionData senderConnectionData = new ConnectionData(receivedPacket.getAddress(), receivedPacket.getPort());
                String sender = getSender(senderConnectionData);
                stringToSendHandler(InputHelper.defineMessageFromInput(input), receivedPacket, true);
                ConnectionData receiverData = clients.get(receiver);

                subtitlesPrinter.printLogSuccessfullySentMessage(sender, receiver);
                return new Packet(bufToSend, receiverData);
            } else {
                subtitlesPrinter.printLogMessageWasNotSent();
            }
        } else if (receiver != null) {
            subtitlesPrinter.printLogSomeoneTriedToSendMessage();
            if (checkIfReceiverIsOnTheList(receiver.toLowerCase(Locale.ROOT))) {
                ConnectionData senderConnectionData = new ConnectionData(receivedPacket.getAddress(), receivedPacket.getPort());
                String sender = getSender(senderConnectionData);

                stringToSendHandler(input, receivedPacket, true);
                ConnectionData receiverData = clients.get(receiver);
                subtitlesPrinter.printLogSuccessfullySentMessage(sender, receiver);
                return new Packet(bufToSend, receiverData);
            } else {
                subtitlesPrinter.printLogMessageWasNotSent();
            }
        }
        overrideAddresses(receivedPacket.getAddress(), receivedPacket.getPort());
        stringToSendHandler("Something went wrong.", receivedPacket, false);
        return new Packet(bufToSend, new ConnectionData(inetAddress, port));


    }
*/
