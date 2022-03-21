package com.jetluo.jcip.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName ThreadLocalTest2
 * @Description TODO
 * @Author jet
 * @Date 2022/3/4 09:58
 * @Version 1.0
 **/
public class ThreadLocalTest2 {

    //(1)创建ThreadLocal变量
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        AtomicInteger atomicInteger= new AtomicInteger();

        System.out.println("AtomicInteger:identityHashCode="+System.identityHashCode(atomicInteger));

        System.out.println("AtomicInteger:incrementAndGet="+atomicInteger.incrementAndGet());

        System.out.println("AtomicInteger:identityHashCode="+System.identityHashCode(atomicInteger));
        AtomicInteger atomicInteger2= new AtomicInteger();
        System.out.println("AtomicInteger2:identityHashCode="+System.identityHashCode(atomicInteger2));
        //在main线程中添加main线程的本地变量
        threadLocal.set("mainVal");


        //新创建一个子线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                threadLocal.set("subVal");
                System.out.println("子线程中的本地变量值:"+threadLocal.get());
            }
        });
        thread.start();
        //输出main线程中的本地变量值
        System.out.println("mainx线程中的本地变量值:"+threadLocal.get());
    }
}