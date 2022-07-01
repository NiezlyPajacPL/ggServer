package managers.commands;

import helpers.Packet;
import managers.commands.messageTypes.MessageType;

import java.net.DatagramPacket;

public interface CommandMapper {

    MessageType mapCommand(Packet packet);
}
