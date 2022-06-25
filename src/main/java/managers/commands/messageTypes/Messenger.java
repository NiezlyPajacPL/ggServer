package managers.commands.messageTypes;

import managers.commands.messageTypes.MessageType;

import java.net.InetAddress;

public class Messenger extends MessageType {

    public String sender;
    public String receiver;
    public String message;
    public InetAddress destinationInetAddress;
    public int destinationPort;
    public byte[] failedToSendMessage;

    public Messenger(String sender, String receiver, String message, InetAddress destinationInetAddress, int destinationPort,byte[] failedToSendMessage){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.destinationInetAddress = destinationInetAddress;
        this.destinationPort = destinationPort;
        this.failedToSendMessage = failedToSendMessage;
    }

}
