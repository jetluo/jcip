package com.jetluo.jcip.chapter08;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;

/**
 * @ClassName ValueLatch
 *  程序清单 8-17
 * @Description 由ConcurrentPuzzleSolver使用的携带结果的闭锁
 * @Author jet
 * @Date 2022/3/17 22:40
 * @Version 1.0
 **/
@ThreadSafe
public class ValueLatch <T>{
    @GuardedBy("this")
    private T value = null;
    private final CountDownLatch done = new CountDownLatch(1);
    public boolean isSet(){
        return (done.getCount() == 0);
    }
    public synchronized void setValue(T newValue){
        if (!isSet()){
            value = newValue;
            //将count值减掉1
            done.countDown();
        }
    }
    public T getValue() throws InterruptedException{
        //线程挂起，等待所有线程执行完毕。
        done.await();
        synchronized (this){
            return value;
        }
    }
}
