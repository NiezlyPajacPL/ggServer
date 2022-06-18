package managers.commandHandlers;

import helpers.CommandData;
import helpers.Packet;
import helpers.StringToSendHelper;
import managers.ConnectionData;
import managers.refactor.MessageType;

import java.net.DatagramPacket;
import java.util.Map;

public class RegisterUser extends MessageType {


    CommandData commandData;
    String nickname;
    DatagramPacket packet;
    Map<String, ConnectionData> clients;

    public RegisterUser(String nickname, DatagramPacket packet,Map<String, ConnectionData> clients){
        this.nickname = nickname;
        this.packet = packet;
        this.clients = clients;
    }

    StringToSendHelper stringToSendHelper;

    public Packet register(){
        addClientToDataBase();
        return new Packet(stringToSendHelper.stringToSendHandler("Registered Successfully",nickname, false),
                new ConnectionData(packet.getAddress(), packet.getPort()));
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

*/
