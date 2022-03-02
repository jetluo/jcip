package com.jetluo.jcip.chapter07;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @ClassName SocketUsingTask
 * @Description 通过newTaskFor将非标准的取消操作封装在一个任务中
 * @Author jet
 * @Date 2022/1/21 15:05
 * @Version 1.0
 **/
public abstract class SocketUsingTask<T> implements CancellableTask<T> {
    private Socket socket;

    protected synchronized void setSocket(Socket s){
        socket = s;
    }

    @Override
    public synchronized void cancel() {
        try{
            if (socket!=null){
                socket.close();
            }
        }catch (IOException ignored){

        }
    }

    @Override
    public RunnableFuture<T> newTask() {
        return new FutureTask<T>(this){
            @Override
            public boolean cancel(boolean mayInterruptIfRunning){
                try{
                    SocketUsingTask.this.cancel();
                }finally {
                    return super.cancel(mayInterruptIfRunning);
                }
            }
        };
    }
}

interface CancellableTask <T> extends Callable<T>{
    /**
     * @Author jet
     * @Description //取消
     * @Date 2022/1/21
     * @Param []
     * @return void
     **/
    void cancel();
    /**
     * @Author jet
     * @Description //新建任务
     * @Date 2022/1/21
     * @Param []
     * @return java.util.concurrent.RunnableFuture<T>
     **/
    RunnableFuture<T> newTask();
}

class CancellingExecutor extends ThreadPoolExecutor{

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable){
        if (callable instanceof CancellableTask){
            return ((CancellableTask<T>) callable).newTask();
        }else {
            return  super.newTaskFor(callable);
        }
    }
}