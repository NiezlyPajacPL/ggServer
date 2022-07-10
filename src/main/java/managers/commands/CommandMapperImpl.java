package managers.commands;

import helpers.Packet;
import helpers.InputHelper;
import helpers.ConnectionData;
import managers.commands.messageTypes.*;

import java.util.Locale;
import java.util.Map;

public class CommandMapperImpl implements CommandMapper {

    Map<String, ConnectionData> clients;
    private final String REGISTER = "/register";
    private final String ALLUSERS = "/allUsers";
    private final String MESSAGE = "/msg";
    private final String LOGIN = "/login";
    private final String LOGOUT = "/logout";

    @Override
    public MessageType mapCommand(Packet receivedPacket) {

        String input = new String(receivedPacket.getData());
        InputHelper inputHelper = new InputHelper();

        if (input.contains(REGISTER)) {
            String name = inputHelper.getFirstArgument(input).replaceAll("[\\s\u0000]+", "").toLowerCase(Locale.ROOT);
            String password = inputHelper.definePasswordFromInput(input).replaceAll("[\\s\u0000]+", "");
            return new Registration(name, password);

        } else if (input.contains(ALLUSERS)) {
            return new UsersListSender();

        } else if (input.contains(MESSAGE)) {
            String receiver = inputHelper.getFirstArgument(input);
            String message = inputHelper.defineMessageFromInput(input);
            return new Messenger(receiver, message);

        } else if (input.contains(LOGIN)) {
            String name = inputHelper.getFirstArgument(input).replaceAll("[\\s\u0000]+", "").toLowerCase(Locale.ROOT);
            String password = inputHelper.definePasswordFromInput(input).replaceAll("[\\s\u0000]+", "");
            return new Login(name, password);

        } else if (input.contains(LOGOUT)) {
            return new Logout();
        }
        return null;
    }

}


