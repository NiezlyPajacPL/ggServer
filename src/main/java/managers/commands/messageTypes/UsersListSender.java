package managers.commands.messageTypes;

import managers.ConnectionData;
import managers.commands.messageTypes.MessageType;

import java.net.InetAddress;

public class UsersListSender extends MessageType {

    public ConnectionData connectionData;

    public UsersListSender(ConnectionData connectionData) {
        this.connectionData = connectionData;
    }

}
