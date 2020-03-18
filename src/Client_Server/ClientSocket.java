package Client_Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
public class ClientSocket {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Scanner scanner;

    public ClientSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.scanner = new Scanner(System.in);
    }

    public void run() throws IOException {
        MsgTransPort.writeToSocket(outputStream, "hello");
        while (true) {
            String msg = MsgTransPort.readFromSocket(inputStream);

            System.out.println("Server receive: ");
            System.out.println(msg);

            String inputString = scanner.nextLine();
            MsgTransPort.writeToSocket(outputStream, inputString);

            // 退出程序
            if ("exit".equals(inputString.toLowerCase())) {
                break;
            }
        }

        scanner.close();
    }
}
