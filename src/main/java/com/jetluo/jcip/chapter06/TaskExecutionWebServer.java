package com.jetluo.jcip.chapter06;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @ClassName TaskExecutionWebServer
 * @Description 基于线程池的Web服务器
 * @Author jet
 * @Date 2022/1/12 22:34
 * @Version 1.0
 **/
public class TaskExecutionWebServer {
    private static final  int THHEADS =100;
    private  static  final Executor exec = Executors.newFixedThreadPool(THHEADS);

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(18000);
        while (true){
            final Socket connection = socket.accept();
            Runnable task = () -> handleRequest(connection);
            exec.execute(task);
        }

    }
    private static  void handleRequest(Socket connection){
        // request-handling logic here
        try {
            System.out.println("请求地址和端口："+connection.getLocalAddress().getHostAddress()+":"+connection.getLocalPort());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
