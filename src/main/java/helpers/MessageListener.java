package helpers;

import java.net.Socket;

public interface MessageListener {

    Socket onMessageReceivedGetReceiverSocket(String receiver);

    void onClientLoggingIn(String nickname);

    void onClientLoggedOut(String nickname);

    String onUsersListRequest();
}
