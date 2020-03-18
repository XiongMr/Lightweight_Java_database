package Client_Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    public static void main(String[] args){
        try {
            Socket socket = new Socket("localhost",8888);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            ClientSocket clientSocket = new ClientSocket(socket);
            clientSocket.run();

            inputStream.close();
            outputStream.close();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
