package com.jetluo.jcip.chapter06;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName ThreadPerTaskWebServer
 * @Description 服务器为每个请求创建一个线程
 * @Author jet
 * @Date 2022/1/11 22:00
 * @Version 1.0
 **/
public class ThreadPerTaskWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket= new ServerSocket(80);
        while (true){
            final Socket connection= socket.accept();
            Runnable task= new Runnable() {
                @Override
                public void run() {
                    handleRequest(connection);
                }
            };
            new Thread(task).start();
        }
    }
    private static void handleRequest(Socket connection){
        // request-handling logic here
        try {
            System.out.println("请求地址和端口："+connection.getLocalAddress().getHostAddress()+":"+connection.getLocalPort());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
