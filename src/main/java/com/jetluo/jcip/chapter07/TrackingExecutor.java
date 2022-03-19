package com.jetluo.jcip.chapter07;

import java.util.*;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TrackingExecutor
 * @Description 在ExecutorService中跟踪在关闭之后被取消的任务
 * @Author jet
 * @Date 2022/3/2 13:31
 * @Version 1.0
 **/
public class TrackingExecutor extends AbstractExecutorService {
    private final ExecutorService exec;

    private final Set<Runnable> taskCancelledAtShutdown = Collections.synchronizedSet(new HashSet<Runnable>());

    public  TrackingExecutor(ExecutorService exec){
        this.exec = exec;
    }
    @Override
    public void shutdown() {
        exec.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return exec.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return exec.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return exec.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return exec.awaitTermination(timeout, unit);
    }
    /**
     * @Author jet
     * @Description //返回被取消的任务
     * @Date 2022/3/2
     * @Param []
     * @return java.util.List<java.lang.Runnable>
     **/
    public List<Runnable> getCancelledTasks(){
        if (!exec.isTerminated()){
            throw new IllegalStateException("线程池未结束！");
        }
        return new ArrayList<Runnable>(taskCancelledAtShutdown);
    }

    @Override
    public void execute(final Runnable runnable) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                }finally {
                    if (isShutdown()&& Thread.currentThread().isInterrupted()){
                        taskCancelledAtShutdown.add(runnable);
                    }
                }

            }
        });
    }
}
