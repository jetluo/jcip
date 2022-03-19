package com.jetluo.jcip.chapter08;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * @ClassName TimingThreadPool
 *  程序清单 8-9
 * @Description 增加了日志和计时等功能等线程池
 * @Author jet
 * @Date 2022/3/10 22:50
 * @Version 1.0
 **/
public class TimingThreadPool extends ThreadPoolExecutor {
    public TimingThreadPool(){
        super(1,1,0L, TimeUnit.SECONDS,null);
    }

    private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    private final Logger log = Logger.getLogger("TimingThreadPool");
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();


    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        log.fine(String.format("Thread %s:start %s", t,r));
        startTime.set(System.nanoTime());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try{
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();
            numTasks.incrementAndGet();
            log.fine(String.format("Thread %s: end %s  time=%dns",t,r,taskTime));
        }finally {
            super.afterExecute(r, t);
        }

    }

    @Override
    protected void terminated() {
        try{
            log.info(String.format("Terminated: avg time=%dns", totalTime.get()/numTasks.get()));
        }finally {

            super.terminated();
        }

    }
}
