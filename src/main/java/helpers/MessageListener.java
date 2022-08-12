package helpers;

import java.net.Socket;
import java.util.Map;

public interface MessageListener {

    Socket onMessageReceivedGetUser(String receiver);

    void onClientLoggingIn(String nickname);

    void onClientLoggedOut(String nickname);

    Map<String, Socket> getUsersList();
}
