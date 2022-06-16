package managers.commandHandlers;

import helpers.CommandData;
import helpers.FileHandler;
import helpers.Packet;
import managers.ConnectionData;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RegisterUser {


    CommandData commandData;
    String nickname;
    DatagramPacket packet;
    Map<String, ConnectionData> clients;

    public RegisterUser(String nickname, DatagramPacket packet,Map<String, ConnectionData> clients){
        this.nickname = nickname;
        this.packet = packet;
        this.clients = clients;
    }
    private byte[] bufToSend;

    public Packet register(){
        addClientToDataBase();
        stringToSendHandler("Registered Successfully", receivedPacket, false);
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
    private void addClientToDataBase() {
        clients.put(nickname, new ConnectionData(packet.getAddress(), packet.getPort()));
     //   clientList.add(nickname);
       /*
        try {
            FileHandler fileHandler = new FileHandler();
            fileHandler.overrideDataBase(nickname + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        */
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


