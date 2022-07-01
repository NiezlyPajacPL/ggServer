package network;

import java.io.*;
import java.net.Socket;

public class TcpClientThread extends Thread{
    protected Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;
    public TcpClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run(){
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message;
        while (true) {
            try {
                message = input.readLine();
                if ((message == null) || message.equalsIgnoreCase("QUIT")) {
                    clientSocket.close();
                    return;
                } else {
                    output.println(message + "\n\r");
                    output.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
