package managers.commands.messageTypes;

public class Registration extends MessageType {

    public String name;
    public String password;

    public Registration(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
