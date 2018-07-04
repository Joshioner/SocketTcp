import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : davie
 * 服务端实体类
 */
public class Server {
    int port;  //端口号
    List<Socket> clients;  //监控的客户端的列表
    ServerSocket server; //服务端对象

    public Server(){
       try {
           port = 8080;
           server = new ServerSocket(port);
           clients = new ArrayList<Socket>();
           //持续监控，监听客户端的连接
           while (true){
               Socket socket= server.accept();
               clients.add(socket);
               String msg = "欢迎 [" + socket.getInetAddress() + " ]加入聊天室，当前聊天室有 " + clients.size() + " 人";
               sendMsg(msg);
               new MyThread(socket).start();
           }
       }catch (Exception ex){
           ex.printStackTrace();
       }
    }

    private void sendMsg(String msg) {
        System.out.println(msg);
        try {
            for (int i = 0 ; i < clients.size();i ++ ){
                //获取客户端的输出流对象，写入数据
                OutputStream out = clients.get(i).getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write(msg + "\r\n");
                //将缓冲区的数据写到输出对象中
                writer.flush();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 线程类：监听客户端的连接
     */
    class MyThread extends Thread{
        Socket socket;
        public MyThread(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
           try {
               InputStream in = socket.getInputStream();
               BufferedReader reader = new BufferedReader(new InputStreamReader(in));
               String line = null;
               //监听客户端发送的数据
               while ((line = reader.readLine())!= null){
                   sendMsg(line);
               }
           }catch (Exception ex){
               ex.printStackTrace();
           }

        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
