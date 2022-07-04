package managers.commands;

import helpers.Packet;
import helpers.PasswordHasher;
import helpers.InputHelper;
import helpers.SecuredPassword;
import managers.ConnectionData;
import managers.commands.messageTypes.*;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CommandMapperImpl implements CommandMapper {

    Map<String, ConnectionData> clients;
    private final String REGISTER = "/register";
    private final String ALLUSERS = "/allUsers";
    private final String MESSAGE = "/msg";
    private final String LOGIN = "/login";
    private final String LOGOUT = "/logout";
    private final String UNKNOWN = "UNKNOWN";

    public CommandMapperImpl(Map<String, ConnectionData> users) {
        this.clients = users;
    }

    @Override
    public MessageType mapCommand(Packet receivedPacket) {

        String input = new String(receivedPacket.getData());
        InputHelper inputHelper = new InputHelper();
        PasswordHasher passwordHasher = new PasswordHasher();

        if (input.contains(REGISTER)) {
            String name = inputHelper.getFirstArgument(input).replaceAll("[\\s\u0000]+", "").toLowerCase(Locale.ROOT);
            String password = inputHelper.definePasswordFromInput(input).replaceAll("[\\s\u0000]+", "");
            SecuredPassword securedPassword = passwordHasher.generateSecuredPassword(password);

            return new Registration(name, securedPassword, receivedPacket.getConnectionData());
        } else if (input.contains(ALLUSERS)) {
            return new UsersListSender(receivedPacket.getConnectionData());
        } else if (input.contains(MESSAGE)) {
            String sender = getSender(receivedPacket.getConnectionData(), clients);
            String receiver = inputHelper.getFirstArgument(input);
            String message = inputHelper.defineMessageFromInput(input);
            ConnectionData receiverData = clients.get(receiver);

            return new Messenger(sender,
                    receiver,
                    message,
                    receiverData.getInetAddress(),
                    receiverData.getPort());
        } else if (input.contains(LOGIN)) {
            String name = inputHelper.getFirstArgument(input).replaceAll("[\\s\u0000]+", "").toLowerCase(Locale.ROOT);
            String password = inputHelper.definePasswordFromInput(input).replaceAll("[\\s\u0000]+", "");
            try {
                return new Login(name,
                        password,
                        receivedPacket.getAddress(),
                        receivedPacket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (input.contains(LOGOUT)) {
            return new Logout(getSender(receivedPacket.getConnectionData(), clients),
                    receivedPacket.getAddress(),
                    receivedPacket.getPort());
        }
        return null;
    }

    private String getSender(ConnectionData senderConnectionData, Map<String, ConnectionData> clients) {
        for (Map.Entry<String, ConnectionData> entry : clients.entrySet()) {
            if(senderConnectionData.getInetAddress()!=null){
            if ((Objects.equals(entry.getValue().getInetAddress(), senderConnectionData.getInetAddress())) && Objects.equals(entry.getValue().getPort(), senderConnectionData.getPort())) {
                return entry.getKey();
            }
            }
        }
        return UNKNOWN;
    }

}


