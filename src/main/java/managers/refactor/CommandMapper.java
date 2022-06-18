package managers.refactor;

import java.net.DatagramPacket;

public interface CommandMapper {

    MessageType mapCommand(DatagramPacket packet);
}
