package managers.commandHandlers;

import helpers.CommandData;
import helpers.Packet;
import helpers.SenderFinder;
import helpers.StringToSendHelper;
import managers.ConnectionData;
import managers.SubtitlesPrinter;

import java.net.DatagramPacket;
import java.util.Map;
import java.util.Objects;

public class CommandUser {

    Map<String, ConnectionData> clients;
    SubtitlesPrinter subtitlesPrinter;

    public CommandUser(Map<String, ConnectionData> clients, SubtitlesPrinter subtitlesPrinter){
        this.clients = clients;
        this.subtitlesPrinter = subtitlesPrinter;
    }
    CommandHandler commandHandler = new CommandHandler();

    public Packet useCommand(DatagramPacket receivedPacket) {
        CommandData commandData = commandHandler.commands(receivedPacket);

        if (Objects.equals(commandData.getType(), "registration")) {
            String sender = SenderFinder.getSender(true,receivedPacket,clients);
            subtitlesPrinter.printLogClientRegistered(sender, commandData.packet.getAddress(), commandHandler.port);

            return new RegisterUser(sender, commandData.packet, clients).register();
        }else if(Objects.equals(commandData.getType(), "message")){

        }

        return new Packet(StringToSendHelper.stringToSendHandler("Something went wrong", null, false),
                new ConnectionData(commandData.packet.getAddress(), commandData.packet.getPort()));
    }
}
