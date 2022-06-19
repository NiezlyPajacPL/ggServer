package helpers;

import java.nio.charset.StandardCharsets;


public class StringToSendHelper {

    public byte[] stringToSendHandler(String text, String sender, boolean senderPrinted) {
        if (senderPrinted) {
            String textToSend = sender + ": " + text;
            return textToSend.getBytes(StandardCharsets.UTF_8);
        }
        return text.getBytes(StandardCharsets.UTF_8);
    }

}
