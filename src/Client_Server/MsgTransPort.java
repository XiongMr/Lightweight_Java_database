package Client_Server;
import java.io.*;

public class MsgTransPort {

    public static String readFromSocket(InputStream inputStream) throws IOException {
        // 先读第一个字节
        int first = inputStream.read();
        // 读取第二个字节
        int second = inputStream.read();
        int length = (first << 8) + second; // 用位运算将两个字节拼起来成为真正的长度

        if (length < 0) {
            return "";
        }

        byte[] bytes = new byte[length]; // 构建指定长度的字节大小来储存消息即可
        inputStream.read(bytes);
        return new String(bytes,"UTF-8");
    }

    public static void writeToSocket(OutputStream outputStream, String msg) throws IOException {
        // 首先要把message转换成bytes以便处理
        byte[] bytes = msg.getBytes("UTF-8");
        // 接下来传输两个字节的长度，依然使用移位实现
        int length = bytes.length;
        outputStream.write(length >> 8); // write默认一次只传输一个字节
        outputStream.write(length);
        // 传输完长度后，再正式传送消息
        outputStream.write(bytes);
    }
}
