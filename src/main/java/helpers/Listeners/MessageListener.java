package helpers.Listeners;

import managers.ConnectionData;

import java.net.Socket;

public interface MessageListener {

    String onMessageReceivedGetSender(ConnectionData senderConnectionData);

    Socket onMessageReceivedGetReceiverSocket(String receiver);
}
