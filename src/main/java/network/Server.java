package network;

import managers.ConnectionData;
import managers.Temp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Server extends Thread {
    private DatagramSocket socket;
    private boolean running;
    private byte[] bufToReceive = new byte[256];
    private byte[] bufToSend;
    private ArrayList<String> clientList = new ArrayList<>();
    Map<String, ConnectionData> clients = new LinkedHashMap<>();
    List<Temp> list = new ArrayList<>();

    public Server() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public void run() {
        running = true;

        while (running) {
            DatagramPacket receivedPacket
                    = new DatagramPacket(bufToReceive, bufToReceive.length);
            DatagramPacket packetToSend;
            try {
                socket.receive(receivedPacket);
                String input = new String(receivedPacket.getData());
                if (input.contains("/register")) {
                    String nickname = defineWhoWantsToRegister(input).replaceAll("\s+","");
                    if (!checkIfNicknameIsAlreadyUsed(nickname)) {
                        list.add(new Temp(nickname,new ConnectionData(receivedPacket.getAddress(), receivedPacket.getPort())));
                        clientList.add(nickname);
                        InetAddress addressRegistered = receivedPacket.getAddress();
                        int portRegistered = receivedPacket.getPort();

                   //     clients.put(nickname, new ConnectionData(addressRegistered, portRegistered));

                        String registeredSuccessfully = "Registered Successfully!";
                        bufToSend = registeredSuccessfully.getBytes(StandardCharsets.UTF_8);
                        packetToSend = new DatagramPacket(bufToSend, bufToSend.length, addressRegistered, portRegistered);
                        socket.send(packetToSend);
                        System.out.println("Registered client " + nickname + " -- IP: " + addressRegistered + ":" + portRegistered);
                    } else {
                        System.out.println("Nickname is already in use.");
                    }

                } else if (input.contains("/allUsers") || input.contains("/allusers") || input.contains("/users")) {
                    System.out.println("Received users list request.");
                    InetAddress address = receivedPacket.getAddress();
                    int port = receivedPacket.getPort();
                    String clientListInString = clientList.toString();
                    bufToSend = clientListInString.getBytes(StandardCharsets.UTF_8);
                    packetToSend = new DatagramPacket(bufToSend, bufToSend.length, address, port);
                    socket.send(packetToSend);
                } else if (input.contains("/msg")) {
                    String receiver = defineReceiver(input).replaceAll("\s+","");
                    String message = defineMessageFromInput(input);
                    System.out.println("Someone tried to send message."); //todo define who tried
                    bufToSend = message.getBytes(StandardCharsets.UTF_8);
                    for (Map.Entry<String, ConnectionData> clients : this.clients.entrySet()) {
                        System.out.println(clients.getKey() + ":" + clients.getValue());
                    }
                    ConnectionData receiverData = temp(receiver);


                    packetToSend = new DatagramPacket(
                            bufToSend,
                            bufToSend.length,
                            receiverData.inetAddress,
                            receiverData.port
                    );
                    socket.send(packetToSend);

                }

                //   packet = new DatagramPacket(buf, buf.length, address, port);
                //   String received
                //           = new String(packet.getData(), 0, packet.getLength());

                //           if (received.equals("end")) {
                //              running = false;
                //             continue;
                //        }
                //           socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    private String defineWhoWantsToRegister(String input) {
        String[] words = input.split(" ");
        return words[1];
    }

    private ConnectionData temp(String key){
        for (Temp temp : list) {
            if (Objects.equals(key.replaceAll("\s+",""), temp.key.replaceAll("\s+",""))) {
                return temp.connectionData;
            }
        }
        return null;
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

