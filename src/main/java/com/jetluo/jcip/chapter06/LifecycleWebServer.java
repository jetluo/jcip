package com.jetluo.jcip.chapter06;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @ClassName LifecycleWebServer
 * @Description 支持关闭操作的web服务器
 * @Author jet
 * @Date 2022/1/13 22:25
 * @Version 1.0
 **/
public class LifecycleWebServer {

    private final ExecutorService exec = Executors.newCachedThreadPool();

    public void start() throws IOException{
        ServerSocket socket = new ServerSocket(8080);
        while (!exec.isShutdown()){
            try{
            final Socket conn = socket.accept();
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    handleRequest(conn);
                }
            });
            }catch (RejectedExecutionException e){
                if (!exec.isShutdown()){
                    log("task submission rejected ",e);
                }
            }
        }
    }
    public void stop(){
        exec.shutdown();
    }
    private void log(String msg,Exception e){
        Logger.getAnonymousLogger().log(Level.WARNING,msg,e);
    }
    void handleRequest(Socket connection){
        Request req = readRequest(connection);
        if (!iShutdownRequest(req)){
            stop();
        }else {
            dispatchRequest(req);
        }
    }
    interface Request{}

    private Request readRequest(Socket s){
        return  null;
    }
    private void dispatchRequest(Request r){}

    private boolean iShutdownRequest(Request r){
        return  false;
    }

}
