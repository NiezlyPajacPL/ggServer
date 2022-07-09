package helpers.Listeners;

import managers.ConnectionData;

import java.net.Socket;
import java.util.ArrayList;

public interface MessageListener {

    Socket onMessageReceivedGetReceiverSocket(String receiver);

    void onClientLoggingIn(String nickname);

    String onUsersListRequest();
}
