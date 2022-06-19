package managers.commands;

import helpers.InputHelper;
import managers.ConnectionData;
import managers.commands.messageTypes.MessageType;
import managers.commands.messageTypes.Messenger;
import managers.commands.messageTypes.Registration;
import managers.commands.messageTypes.UsersListSender;

import java.net.DatagramPacket;
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

        if (input.contains("/register")) {
            String name = inputHelper.defineWhoWantsToRegister(input).replaceAll("[\\s\u0000]+", "").toLowerCase(Locale.ROOT);
            return  new Registration(name,receivedPacket.getAddress(),receivedPacket.getPort());

        } else if (input.contains("/allUsers") || input.contains("/allusers") || input.contains("/users")) {
           return new UsersListSender(receivedPacket.getAddress(),receivedPacket.getPort());
        }else if(input.contains("/msg")){
            String sender = getSender(receivedPacket,clients);
            String receiver = inputHelper.defineReceiver(input);
            String message = inputHelper.defineMessageFromInput(input);
            ConnectionData receiverData = clients.get(receiver);

            return new Messenger(sender,receiver,message,receiverData.getInetAddress(),receiverData.getPort());
        }
        return  null;
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


