package managers.refactor;

import helpers.InputHelper;
import helpers.SenderFinder;
import managers.ConnectionData;

import java.net.DatagramPacket;
import java.util.HashMap;
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
        SenderFinder senderFinder = new SenderFinder();

        if (input.contains("/register")) {
            String name = inputHelper.defineWhoWantsToRegister(input);;
            return  new Registration(name,receivedPacket.getAddress(),receivedPacket.getPort());

        } else if (input.contains("/allUsers") || input.contains("/allusers") || input.contains("/users")) {
           return new UsersListSender(receivedPacket.getAddress(),receivedPacket.getPort());
        //    return "UsersListRequest";
        }else if(input.contains("/msg")){
            String sender = senderFinder.getSender(false,receivedPacket,clients);
            String receiver = inputHelper.defineReceiver(input);
            String message = inputHelper.defineMessageFromInput(input);
            ConnectionData receiverData = clients.get(receiver);

            return new Messenger(sender,receiver,message,receiverData.getInetAddress(),receiverData.getPort());
        }
        return  null;
       // return "UNKOWN";
    }
}


