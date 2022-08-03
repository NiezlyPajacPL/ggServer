package helpers;

import java.net.Socket;

public interface MessageListener {

    Socket onMessageReceivedGetUser(String receiver);

    void onClientLoggingIn(String nickname);

    void onClientLoggedOut(String nickname);

    String getUsersList();
}
