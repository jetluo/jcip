package com.jetluo.jcip.chapter06;

import java.util.concurrent.Executor;

/**
 * @ClassName ThreadPerTaskExecutor
 * @Description 为每个请求启动一个新线程的Executor
 * @Author jet
 * @Date 2022/1/13 21:44
 * @Version 1.0
 **/
public class ThreadPerTaskExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
