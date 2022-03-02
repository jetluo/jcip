package com.jetluo.jcip.chapter07;

import org.omg.CORBA.TIMEOUT;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.*;

/**
 * @ClassName LogService
 * @Description 向LogWriter添加可靠的取消操作
 * @Author jet
 * @Date 2022/2/16 09:01
 * @Version 1.0
 **/
public class LogService2 {
    private  final ExecutorService exec = Executors.newSingleThreadExecutor();
    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private final PrintWriter writer;
    private boolean isShutdown;
    private  int reservations;

    public LogService2(Writer writer){
        this.queue = new LinkedBlockingQueue<String>();
        this.loggerThread = new LoggerThread();
        this.writer = new PrintWriter(writer);
    }

    public void start(){
        loggerThread.start();
    }
    public void stop() throws InterruptedException{
        try {
            exec.shutdown();
            exec.awaitTermination(1, TimeUnit.SECONDS);
        }finally {
            writer.close();
        }

    }
    public void log(String msg) throws  InterruptedException{

        try{
         //   exec.execute(new WriteTask(msg));
        }catch (RejectedExecutionException ignored){

        }
    }

    private class LoggerThread extends Thread{
        @Override
        public void run() {
            try{
                while (true){
                    try {
                        synchronized (LogService2.this){
                            if (isShutdown && reservations ==0){
                                break;
                            }
                        }
                        String msg = queue.take();
                        synchronized (LogService2.this){
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
