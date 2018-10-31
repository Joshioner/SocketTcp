# SocketTcp
基于socket的控制台多人聊天室

#常见问题
1.readline()问题：客户端监听键盘的输入，通过readline()读取输入的数据

               //获取socket的输出流，写出数据
               OutputStream out = socket.getOutputStream();
               BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
               //获取键盘输入流
               InputStream in = System.in;
               BufferedReader reader = new BufferedReader(new InputStreamReader(in));
               String line = null;
               while ((line = reader.readLine()) != null){
                   //写入键盘输入的数据
                   writer.write(line );
                   writer.flush(); //记得调用此方法，将缓冲区的数据写出，否则数据一直存储在缓冲区中，直到关闭
               }
               
 出现的问题：键盘不断输入，不断回车，然而服务端却没有显示客户端发送的数据
 
 原因：跟readline()原理有关，查看源代码可知，readline()读取到回车键或者\r\n的时候，表述读取一行数据结束，此时，readline()会将回车键或者\r\n去掉，
 只是将读取数据，不包括回车键。
 那么我们现在来看这个问题，当我们键盘输入的时候，readline()读取数据，将\r\n去掉，发送给服务端，服务端接受数据,通过readline()读取数据，然而上面的原理可知，
 readline()只有读取到回车键或者\r\n等的时候才表示一行数据读取结束，而客户端发送给服务端的数据却是去掉\r\n后的数据，所以服务端一直处于阻塞状态，一直没有显示
 
 解决方案：writer.write(line + "\r\n");在后面加上\r\n即可
 
 
 
 2.客户端问题：客户端有两个任务，一个是监听键盘的输入，一个是监听服务端的数据，为了避免只是一次性接受数据，我们一般使用while进行监听，
 然而一不小心，就会导致死循环.
 
          //获取socket的输出流，写出数据
            OutputStream out = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            //获取键盘输入流
            InputStream inputStream = System.in;
            BufferedReader Bufreader = new BufferedReader(new InputStreamReader(inputStream));
            String msg = null;
            while ((msg = Bufreader.readLine()) != null){
                //写入键盘输入的数据
                writer.write(msg + "\r\n");
                writer.flush(); //记得调用此方法，将缓冲区的数据写出，否则数据一直存储在缓冲区中，直到关闭
            }
            //获取socket的输入流
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            //接收客户端的数据
            while ((line = reader.readLine())!= null){
                System.out.println(line);
            }
 问题：我们先调用while循环，进行监听键盘的输入，然而readline()是一个阻塞方法，当没有数据读取的时候，则一直处于阻塞状态，导致后面的监听服务端的数据无法执行
 解决方法：为了两个while能够同时执行，则利用并行（多线程技术，则可以完美解决这个缺陷）
 
 
