package com.jetluo.jcip.chapter07;

import java.util.concurrent.*;

import static com.jetluo.jcip.chapter05.LaunderThrowable.launderThrowable;

/**
 * @ClassName TimedRun3
 * @Description 通过Future来取消任务
 * @Author jet
 * @Date 2022/1/21 10:58
 * @Version 1.0
 **/
public class TimedRun3 {
    private static final ExecutorService taskExec = Executors.newCachedThreadPool();

    public static void timedRun3(Runnable r, long timeout, TimeUnit unit) throws  InterruptedException{
        Future<?> task = taskExec.submit(r);
        try{
            task.get(timeout, unit);
        }catch (TimeoutException e){
            //task will be canceled below 接下来任务将被取消
        }catch (ExecutionException e){
            // exception thrown in task; rethrow 如果在任务中抛出了异常，那么重新抛出该异常
            throw launderThrowable(e.getCause());
        }finally {
            task.cancel(true);
        }

    }
}
