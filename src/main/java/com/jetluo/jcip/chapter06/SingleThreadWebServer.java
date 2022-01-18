package com.jetluo.jcip.chapter06;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @ClassName SingleThreadWebServer
 * @Description 只能处理一个请求用户的web服务器
 * @Author jet
 * @Date 2022/1/11 21:48
 * @Version 1.0
 **/
public class SingleThreadWebServer {

    public static void main(String[] args) throws IOException {
        ServerSocket socket=new ServerSocket(80);
        while (true){
            Socket connection= socket.accept();
            handleRequest(connection);
        }
    }
    private static void handleRequest(Socket connection){
        // request-handling logic here
        try {
            System.out.println(connection.getInetAddress());
            System.out.println(connection.getLocalPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
