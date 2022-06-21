package managers.commands;

import helpers.HashPassword;
import helpers.InputHelper;
import managers.ConnectionData;
import managers.commands.messageTypes.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CommandMapperImplementation implements CommandMapper {

    Map<String, ConnectionData> clients;

    public CommandMapperImplementation(Map<String, ConnectionData> clients) {
        this.clients = clients;
    }

    @Override
    public MessageType mapCommand(DatagramPacket receivedPacket) {

        String input = new String(receivedPacket.getData());
        InputHelper inputHelper = new InputHelper();
        HashPassword hashPassword = new HashPassword();

        if (input.contains("/register")) {
            String name = inputHelper.defineWhoWantsToRegister(input).replaceAll("[\\s\u0000]+", "").toLowerCase(Locale.ROOT);
            String password = inputHelper.definePasswordFromInput(input).replaceAll("[\\s\u0000]+", "");
            String hashedPassword = hashPassword.generateSecuredPassword(password);

            return new Registration(name,
                    hashedPassword,
                    receivedPacket.getAddress(),
                    receivedPacket.getPort(),
                    "Registered Successfully!".getBytes(StandardCharsets.UTF_8),
                    "Nickname is already taken. :(".getBytes(StandardCharsets.UTF_8));

        } else if (input.contains("/allUsers") || input.contains("/allusers") || input.contains("/users")) {
            return new UsersListSender(receivedPacket.getAddress(), receivedPacket.getPort(), clients.toString().getBytes(StandardCharsets.UTF_8));
        } else if (input.contains("/msg")) {
            String sender = getSender(receivedPacket, clients);
            String receiver = inputHelper.defineReceiver(input);
            String message = inputHelper.defineMessageFromInput(input);
            ConnectionData receiverData = clients.get(receiver);

            return new Messenger(sender, receiver, message, receiverData.getInetAddress(), receiverData.getPort());
        } else if (input.contains("/login")) {
            String name = inputHelper.defineWhoWantsToRegister(input).replaceAll("[\\s\u0000]+", "").toLowerCase(Locale.ROOT);
            String password = inputHelper.definePasswordFromInput(input).replaceAll("[\\s\u0000]+", "");
            String message = "Hello again " + name + "!";
            try {
                return new Login(name,
                        password,
                        receivedPacket.getAddress(),
                        receivedPacket.getPort(),
                        message.getBytes(StandardCharsets.UTF_8),
                        "Something went wrong. Try again".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getSender(DatagramPacket packet, Map<String, ConnectionData> clients) {
        for (Map.Entry<String, ConnectionData> entry : clients.entrySet()) {
            if ((Objects.equals(entry.getValue().getInetAddress(), packet.getAddress())) && Objects.equals(entry.getValue().getPort(), packet.getPort())) {
                return entry.getKey();
            }
        }
        return "UNKNOWN";
    }
}


