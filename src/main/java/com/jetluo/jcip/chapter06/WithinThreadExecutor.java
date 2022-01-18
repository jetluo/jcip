package com.jetluo.jcip.chapter06;

import java.util.concurrent.Executor;

/**
 * @ClassName WithinThreadExecutor
 * @Description 在调用线程中以同步方式执行所有任务的Executor
 * @Author jet
 * @Date 2022/1/13 21:47
 * @Version 1.0
 **/
public class WithinThreadExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
