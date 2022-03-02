package com.jetluo.jcip.chapter07;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName LogWriter
 * @Description 不支持关闭的生产者 - 消费者日志服务
 * @Author jet
 * @Date 2022/1/27 16:00
 * @Version 1.0
 **/
public class LogWriter {
    private final BlockingQueue<String> queue;

    private final LoggerThread logger;

    private static final int CAPACITY = 1000;

    private  boolean shutdownRequested = false;

    public LogWriter(Writer writer) {
        this.queue = new LinkedBlockingQueue<String>(CAPACITY);
        this.logger = new LoggerThread(writer);
    }

    private class LoggerThread extends Thread {

        private final PrintWriter writer;

        public LoggerThread(Writer writer) {
            this.writer = new PrintWriter(writer, true);
        }

        public void log(String msg)throws  InterruptedException{
            if (!shutdownRequested){
                queue.put(msg);
            }else{
                throw new InterruptedException("logger is shut down");
            }

        }

        @Override
        public void run() {
            try {
                while (true) {
                    writer.println(queue.take());
                }
            } catch (InterruptedException ignored) {

            } finally {
                writer.close();
            }
        }
    }

}
