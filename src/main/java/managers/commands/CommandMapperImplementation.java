package managers.commands;

import helpers.HashPassword;
import helpers.InputHelper;
import helpers.SecuredPassword;
import managers.ConnectionData;
import managers.commands.messageTypes.*;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CommandMapperImplementation implements CommandMapper {

    Map<String, ConnectionData> clients;
    private final String REGISTER = "/register";
    private final String ALLUSERS = "/allUsers";
    private final String MESSAGE = "/msg";
    private final String LOGIN = "/login";
    private final String LOGOUT = "/logout";
    private final String UNKNOWN = "UNKNOWN";

    public CommandMapperImplementation(Map<String, ConnectionData> clients) {
        this.clients = clients;
    }

    @Override
    public MessageType mapCommand(DatagramPacket receivedPacket) {

        String input = new String(receivedPacket.getData());
        InputHelper inputHelper = new InputHelper();
        HashPassword hashPassword = new HashPassword();

        if (input.contains(REGISTER)) {
            String name;
            String password;
            SecuredPassword securedPassword;
            if(inputHelper.checkIfInputLengthMatchesExpected(3,input)) {
                name = inputHelper.defineWhoWantsToRegister(input).replaceAll("[\\s\u0000]+", "").toLowerCase(Locale.ROOT);
                password = inputHelper.definePasswordFromInput(input).replaceAll("[\\s\u0000]+", "");
                securedPassword = hashPassword.generateSecuredPassword(password);
            }else{
                name = null;
                password = null;
                securedPassword = null;
            }
            return new Registration(name,
                    securedPassword,
                    receivedPacket.getAddress(),
                    receivedPacket.getPort(),
                    "Registered Successfully!".getBytes(StandardCharsets.UTF_8),
                    "Something went wrong or nickname is already taken. :(".getBytes(StandardCharsets.UTF_8));

        } else if (input.contains(ALLUSERS)) {

          return new UsersListSender(receivedPacket.getAddress(), receivedPacket.getPort(), clients.toString().getBytes(StandardCharsets.UTF_8));
        } else if (input.contains(MESSAGE)) {
            String sender = getSender(receivedPacket, clients);
            String receiver = inputHelper.defineReceiver(input);
            String message = inputHelper.defineMessageFromInput(input);
            ConnectionData receiverData = clients.get(receiver);

            return new Messenger(sender,
                    receiver,
                    message,
                    receiverData.getInetAddress(),
                    receiverData.getPort(),
                    "Message wasn't sent. The user you are trying to reach is offline or does not exist.".getBytes(StandardCharsets.UTF_8));
        } else if (input.contains(LOGIN)) {
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
        }else if(input.contains(LOGOUT)){
            return new Logout(getSender(receivedPacket,clients),
                    receivedPacket.getAddress(),
                    receivedPacket.getPort(),
                    "Successfully logged out. See you soon!".getBytes(StandardCharsets.UTF_8));
        }
        return null;
    }

    public String getSender(DatagramPacket packet, Map<String, ConnectionData> clients) {
        for (Map.Entry<String, ConnectionData> entry : clients.entrySet()) {
            if ((Objects.equals(entry.getValue().getInetAddress(), packet.getAddress())) && Objects.equals(entry.getValue().getPort(), packet.getPort())) {
                return entry.getKey();
            }
        }
        return UNKNOWN;
    }
}


