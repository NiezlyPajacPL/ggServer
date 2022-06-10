package managers;

import helpers.PacketInformation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CommandHandler {
    DatagramSocket socket;
    private boolean running;
    private byte[] bufToReceive = new byte[256];
    private byte[] bufToSend;
    private ArrayList<String> clientList = new ArrayList<>();
    Map<String, ConnectionData> clients = new LinkedHashMap<>();
    List<Temp> list = new ArrayList<>();
    InetAddress inetAddress;
    int port;
    SubtitlesPrinter subtitlesPrinter;
    DatagramPacket packetToSend;
    DatagramSocket datagramSocket;

    public CommandHandler(DatagramSocket socket) throws SocketException {
        this.socket = socket;
    }


    public PacketInformation commands(DatagramPacket receivedPacket) {
        String input = new String(receivedPacket.getData());

        if (input.contains("/register")) {
            String nickname = defineWhoWantsToRegister(input).replaceAll("[\\s\u0000]+", "");
            ;
            if (!checkIfNicknameIsAlreadyUsed(nickname)) {
                overrideAddresses(receivedPacket.getAddress(), receivedPacket.getPort());
                addClientToDataBase(nickname);

                stringToSendHandler("Registered Successfully");
                subtitlesPrinter.printLogClientRegistered(nickname, inetAddress, port);
                return new PacketInformation(bufToSend, new ConnectionData(inetAddress, port));
            } else {
                stringToSendHandler("Nickname is already in use.");
                return new PacketInformation(bufToSend, new ConnectionData(inetAddress, port));
            }

        } else if (input.contains("/allUsers") || input.contains("/allusers") || input.contains("/users")) {
            subtitlesPrinter.printLogUsersListRequest();
            overrideAddresses(receivedPacket.getAddress(), receivedPacket.getPort());
            String clientListInString = clientList.toString();
            bufToSend = clientListInString.getBytes(StandardCharsets.UTF_8);
            return new PacketInformation(bufToSend, new ConnectionData(inetAddress, port));
        } else if (input.contains("/msg")) {
            subtitlesPrinter.printLogSomeoneTriedToSendMessage(); //todo define who tried
            String receiver = defineReceiver(input).replaceAll("[\\s\u0000]+", "");
            char[] receiverInCharArray = receiver.toCharArray();

            stringToSendHandler(defineMessageFromInput(input));
            ConnectionData receiverData = temp(receiverInCharArray);

         /*       packetToSend = new DatagramPacket(
                        bufToSend,
                        bufToSend.length,
                        receiverData.inetAddress,
                        receiverData.port
                );
                socket.send(packetToSend);
*/
        }
        overrideAddresses(receivedPacket.getAddress(), receivedPacket.getPort());
        stringToSendHandler("Something went wrong.");
        return new PacketInformation(bufToSend, new ConnectionData(inetAddress, port));
    }


    private String defineWhoWantsToRegister(String input) {
        String[] words = input.split(" ");
        return words[1];
    }

    private void addClientToDataBase(String nickname) {
        list.add(new Temp(nickname.toCharArray(), new ConnectionData(inetAddress, port)));
        clientList.add(nickname);
        clients.put(nickname, new ConnectionData(inetAddress, port));
    }

    private void stringToSendHandler(String text) {
        bufToSend = text.getBytes(StandardCharsets.UTF_8);
    }

    private ConnectionData temp(char[] key) {
        for (Temp temp : list) {
            if (Arrays.equals(key, temp.key)) {
                return temp.connectionData;
            }
        }
        return null;
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

    private String defineReceiver(String input) {
        String[] words = input.split(" ");
        return words[1];
    }

    private static String defineMessageFromInput(String input) {
        String[] words = input.split(" ", 3);
        return words[2];
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
