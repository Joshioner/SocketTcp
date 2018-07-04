import java.io.*;
import java.net.Socket;

/**
 * @author :davie
 * 客户端实体类
 */
public class Client {
    int port; //端口号
    String host;  //主机
    Socket socket;  //客户端对象
    public Client(){
        try {
            //初始化对象
            port = 8080;
            host = "127.0.0.1";
            socket = new Socket(host,port);
            //开启线程：获取键盘输入
            new MyThread().start();
            //获取socket的输入流
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            //接收客户端的数据
            while ((line = reader.readLine())!= null){
                System.out.println(line);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 线程类：获取键盘的输入
     */
    class MyThread extends Thread{
        @Override
        public void run() {
           try {
               //获取socket的输出流，写出数据
               OutputStream out = socket.getOutputStream();
               BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
               //获取键盘输入流
               InputStream in = System.in;
               BufferedReader reader = new BufferedReader(new InputStreamReader(in));
               String line = null;
               while ((line = reader.readLine()) != null){
                   line = "[ " + socket.getInetAddress() + " ]说：" + line;
                   //写入键盘输入的数据
                   writer.write(line + "\r\n");
                   writer.flush(); //记得调用此方法，将缓冲区的数据写出，否则数据一直存储在缓冲区中，直到关闭
               }
           }catch (Exception ex){
               ex.printStackTrace();
           }

        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
