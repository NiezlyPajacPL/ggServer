package managers.commands.messageTypes;

import managers.commands.messageTypes.MessageType;

import java.net.InetAddress;

public class Registration extends MessageType {

    public String name;
    public InetAddress inetAddress;
    public int port;

    public Registration(String name, InetAddress inetAddress, int port) {
        this.name = name;
        this.inetAddress = inetAddress;
        this.port = port;
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
