package helpers;

import managers.ConnectionData;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class StringToSendHelper {

    public void stringToSendHandler(String text, DatagramPacket receivedPacket, boolean senderRequired) {
        if (senderRequired) {
            String sender = getSender(new ConnectionData(receivedPacket.getAddress(), receivedPacket.getPort()));
            String textToSend = sender + ": " + text;
            bufToSend = textToSend.getBytes(StandardCharsets.UTF_8);
        } else {
            bufToSend = text.getBytes(StandardCharsets.UTF_8);
        }
    }
    private String getSender(ConnectionData senderConnectionData) {
        for (Map.Entry<String, ConnectionData> entry : clients.entrySet()) {
            if ((Objects.equals(entry.getValue().getInetAddress(), senderConnectionData.getInetAddress())) && Objects.equals(entry.getValue().getPort(), senderConnectionData.getPort())) {
                return entry.getKey();
            }
        }
        return "UNKNOWN";
    }
}
