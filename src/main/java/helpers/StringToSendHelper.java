package helpers;

import managers.ConnectionData;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class StringToSendHelper {

    public static byte[] stringToSendHandler(String text, String sender, boolean senderPrinted) {
        if (senderPrinted) {
            String textToSend = sender + ": " + text;
            return textToSend.getBytes(StandardCharsets.UTF_8);
        }
        return text.getBytes(StandardCharsets.UTF_8);
    }

}
