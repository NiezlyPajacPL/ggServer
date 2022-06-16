package managers.commandHandlers;

import helpers.CommandData;
import helpers.Packet;

import java.util.Objects;

public class CommandUser {

    public Packet useCommand(CommandData commandData){
        if(Objects.equals(commandData.getType(), "registration")){
            RegisterUser registerUser = new RegisterUser(commandData.getSender(), commandData.packet,commandData.clients);
        }
    }
}
