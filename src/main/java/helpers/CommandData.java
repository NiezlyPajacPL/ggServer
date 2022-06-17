package helpers;

import managers.ConnectionData;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Locale;
import java.util.Map;

public class CommandData {
   private String type;
   public DatagramPacket packet;

    public CommandData(String type, DatagramPacket packet){
        this.type = type;
        this.packet = packet;
    }

    public String getType() {
        return type;
    }
}
