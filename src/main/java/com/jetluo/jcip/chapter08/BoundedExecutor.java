package com.jetluo.jcip.chapter08;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * @ClassName BoundedExecutor
 * 程序清单 8-4
 * @Description 使用Semaphore来控制任务的提交速率
 * @Author jet
 * @Date 2022/3/9 16:31
 * @Version 1.0
 **/
@ThreadSafe
public class BoundedExecutor {
    private final Executor exec;

    private final Semaphore semaphore;

    public BoundedExecutor(Executor exec,int bound){
        this.exec =exec;
        this.semaphore = new Semaphore(bound);
    }

    public void submitTask(final Runnable command) throws InterruptedException{
        semaphore.acquire();
        try {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        command.run();
                    }finally {
                        semaphore.release();
                    }
                }
            });
        }catch (RejectedExecutionException e){
            semaphore.release();
        }
    }
}
