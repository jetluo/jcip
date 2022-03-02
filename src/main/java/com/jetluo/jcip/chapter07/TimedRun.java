package com.jetluo.jcip.chapter07;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TimeRun
 * @Description 在外部线程中安排中断，不建议
 * @Author jet
 * @Date 2022/1/20 15:35
 * @Version 1.0
 **/
public class TimedRun {
    private static  final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);

    public static void timedRun(Runnable r, long timeout, TimeUnit unit){
        final Thread taskThread = Thread.currentThread();
        cancelExec.schedule(new Runnable() {
            @Override
            public void run() {
                taskThread.interrupt();
            }
        }, timeout, unit);
        r.run();
    }
}
