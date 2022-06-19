package managers.commands;

import managers.commands.messageTypes.MessageType;

import java.net.DatagramPacket;

public interface CommandMapper {

    MessageType mapCommand(DatagramPacket packet);
}
