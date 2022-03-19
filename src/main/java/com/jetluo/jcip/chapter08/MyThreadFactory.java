package com.jetluo.jcip.chapter08;

import java.util.concurrent.ThreadFactory;

/**
 * @ClassName MyThreadFactory
 *  程序清单 8-6
 * @Description 自定义的线程工厂
 * @Author jet
 * @Date 2022/3/9 22:20
 * @Version 1.0
 **/
public class MyThreadFactory implements ThreadFactory {
    private final String poolName;

    public MyThreadFactory(String poolName){
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new MyAppThread(r,poolName);
    }
}
