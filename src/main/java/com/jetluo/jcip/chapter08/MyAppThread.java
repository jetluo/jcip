package com.jetluo.jcip.chapter08;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @ClassName MyAppThread
 *  程序清单 8-7
 * @Description 定制Thread基类
 * @Author jet
 * @Date 2022/3/9 22:35
 * @Version 1.0
 **/
public class MyAppThread extends Thread{
    private static final String DEFAULT_NAME = "MyAppThread";
    private static volatile boolean debugLifecycle = false;
    private static final AtomicInteger created = new AtomicInteger();
    private static final AtomicInteger alive = new AtomicInteger();
    private static final Logger log = Logger.getAnonymousLogger();
    public MyAppThread(Runnable r){
        this(r,DEFAULT_NAME);
    }
    public MyAppThread(Runnable runnable,String name){
        super(runnable,name+'-'+created.incrementAndGet());
        setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                log.log(Level.SEVERE,"UNCAUGHT in thread " + t.getName(),e);
            }
        });
    }

    @Override
    public void run() {
        boolean debug = debugLifecycle;
        if (debug){
            log.log(Level.FINE,"Created " + getName());
        }
        try {
            alive.decrementAndGet();
            super.run();

        }finally {
            alive.decrementAndGet();
            if (debug){
                log.log(Level.FINE,"Exiting " + getName());
            }
        }
    }

    public static int getThreadsCreated(){
        return created.get();
    }
    public static int getThreadAlive(){
        return alive.get();
    }
    public static boolean getDebug(){
        return  debugLifecycle;
    }
    public static void setDebug(boolean b){
        debugLifecycle = b;
    }


}
