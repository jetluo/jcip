package com.jetluo.jcip.chapter07;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName LogService
 * @Description 向LogWriter添加可靠的取消操作
 * @Author jet
 * @Date 2022/2/16 09:01
 * @Version 1.0
 **/
public class LogService {
    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private final PrintWriter writer;
    private boolean isShutdown;
    private  int reservations;

    public LogService(Writer writer){
        this.queue = new LinkedBlockingQueue<String>();
        this.loggerThread = new LoggerThread();
        this.writer = new PrintWriter(writer);
    }

    public void start(){
        loggerThread.start();
    }
    public void stop(){
        synchronized (this){
            isShutdown = true;
        }
        loggerThread.interrupt();
    }
    public void log(String msg) throws  InterruptedException{
        synchronized (this){
            if (isShutdown) {
                throw new IllegalStateException("错误");
            }
            reservations++;
        }
        queue.put(msg);
    }

    private class LoggerThread extends Thread{
        @Override
        public void run() {
            try{
                while (true){
                    try {
                        synchronized (LogService.this){
                            if (isShutdown && reservations ==0){
                                break;
                            }
                        }
                        String msg = queue.take();
                        synchronized (LogService.this){
                            reservations--;
                        }
                        writer.println(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }finally {
                writer.close();
            }
        }
    }
}
