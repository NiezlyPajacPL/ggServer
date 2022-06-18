package helpers;

import managers.ConnectionData;

import java.net.DatagramPacket;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SenderFinder {
    public String getSender(boolean senderWantsToRegister, DatagramPacket packet, Map<String, ConnectionData> clients) {
        if(senderWantsToRegister) {
            return InputHelper.defineWhoWantsToRegister(new String(packet.getData()));
        }
        for (Map.Entry<String, ConnectionData> entry : clients.entrySet()) {
            if ((Objects.equals(entry.getValue().getInetAddress(), packet.getAddress())) && Objects.equals(entry.getValue().getPort(), packet.getPort())) {
                return entry.getKey();
            }
        }
        return "UNKNOWN";
    }
}
