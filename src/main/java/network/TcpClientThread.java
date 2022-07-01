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
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            InputStream input = clientSocket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(input));
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    clientSocket.close();
                    return;
                } else {
                    out.writeBytes(line + "\n\r");
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
