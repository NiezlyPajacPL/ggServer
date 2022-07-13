package helpers;

import java.net.Socket;

public interface MessageListener {

    Socket onMessageReceived(String receiver);

    void onClientLoggingIn(String nickname);

    void onClientLoggedOut(String nickname);

    String getUsersList();
}
