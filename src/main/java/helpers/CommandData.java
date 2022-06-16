package helpers;

import managers.ConnectionData;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Map;

public class CommandData {
   private String type;
   private String receiver;
   private String sender;
   public DatagramPacket packet;
   public Map<String, ConnectionData> clients;

    public CommandData(String type, String receiver, String sender,DatagramPacket packet){
        this.type = type;
        this.receiver = receiver;
        this.sender = sender;
        this.packet = packet;
    }

    public CommandData(String type,String sender, DatagramPacket packet,Map<String, ConnectionData> clients){
        this.type = type;
        this.sender = sender;
        this.packet = packet;
        this.clients = clients;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public String getType() {
        return type;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

}
