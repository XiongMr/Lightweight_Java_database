package Client_Server;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8888);
        System.out.println("等待与客户端的链接");
        // 线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        while (true) {
            Socket socket = server.accept();
            System.out.println("客户端"+socket.getPort()+"接入成功");
            Runnable runnable = () -> {
                try {
                    // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
                    ServerThread serverThread = new ServerThread(socket);
                    serverThread.run();
                    System.out.println("client disconnect:" + socket);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            threadPool.submit(runnable);
        }
    }
}
