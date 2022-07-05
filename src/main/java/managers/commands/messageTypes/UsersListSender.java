package managers.commands.messageTypes;

import managers.ConnectionData;

public class UsersListSender extends MessageType {

    public ConnectionData connectionData;

    public UsersListSender(ConnectionData connectionData) {
        this.connectionData = connectionData;
    }

}
