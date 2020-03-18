package Client_Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread{
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;

    public ServerThread(Socket socket) throws IOException{
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }
    public void run(){
        while (true){
            try {
                //读取客户端写入的数据
                String client_msg = MsgTransPort.readFromSocket(inputStream);
                if("exit".equals(client_msg.toLowerCase())){
                    System.out.println("client disconnected!");
                    break;
                }
                else if("hello".equals(client_msg.toLowerCase())){
                    MsgTransPort.writeToSocket(outputStream,"hello!(from server)");
                    continue;
                }
                System.out.println("From client[Port:"+socket.getPort()+"]消息内容："+client_msg);
                //判断是否为SQL语句，如果是，则执行
                if(
                        client_msg.contains("select")||
                                client_msg.contains("insert")||
                                client_msg.contains("delete")||
                                client_msg.contains("update")||
                                client_msg.contains("create")||
                                client_msg.contains("drop")){
                    //执行SQL语句
                    MsgTransPort.writeToSocket(outputStream,"Success!");
                }
                //否则回复:语句不合法
                else{
                    String server_msg = "Statement illegal!";
                    MsgTransPort.writeToSocket(outputStream,server_msg);
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}