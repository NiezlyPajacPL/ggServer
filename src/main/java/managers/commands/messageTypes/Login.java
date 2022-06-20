package managers.commands.messageTypes;

import java.io.IOException;
import java.net.InetAddress;

public class Login extends MessageType {

    public String name;
    public String password;
    public InetAddress inetAddress;
    public int port;
    public byte[] messageSuccessfullyLogged;
    public byte[] messageFailedLogin;

    public Login(String name,String password ,InetAddress inetAddress, int port, byte[] message,byte[] messageFailedLogin) throws IOException {
        this.name = name;
        this.password = password;
        this.inetAddress = inetAddress;
        this.port = port;
        this.messageSuccessfullyLogged = message;
        this.messageFailedLogin = messageFailedLogin;
    }

}
