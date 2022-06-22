package managers.commands.messageTypes;

import helpers.SecuredPassword;

import java.net.InetAddress;

public class Registration extends MessageType {

    public String name;
    public SecuredPassword securedPassword;
    public InetAddress inetAddress;
    public int port;
    public byte[] messageSuccessfullyRegistered;
    public byte[] messageFailedRegistration;

    public Registration(String name, SecuredPassword password, InetAddress inetAddress, int port, byte[] message, byte[] messageFailedRegistration) {
        this.name = name;
        this.securedPassword = password;
        this.inetAddress = inetAddress;
        this.port = port;
        this.messageSuccessfullyRegistered = message;
        this.messageFailedRegistration = messageFailedRegistration;
    }


    private void addClientToDataBase() {
        //  clients.put(nickname, new ConnectionData(packet.getAddress(), packet.getPort()));
        //   clientList.add(nickname);
       /*
        try {
            FileHandler fileHandler = new FileHandler();
            fileHandler.overrideDataBase(nickname + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        */
    }
}
