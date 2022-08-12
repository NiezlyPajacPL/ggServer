package managers.commands;

import helpers.Packet;
import managers.commands.messageTypes.MessageType;

public interface CommandMapper {

    MessageType mapCommand(Packet packet);
}
