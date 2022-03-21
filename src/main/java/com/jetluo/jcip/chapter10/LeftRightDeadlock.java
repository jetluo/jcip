package com.jetluo.jcip.chapter10;

/**
 * @ClassName LeftRightDeadlock
 *  程序清单 10-1
 *  简单的锁顺序死锁（不要这样做）
 * @Description
 * @Author jet
 * @Date 2022/3/19 09:15
 * @Version 1.0
 **/
public class LeftRightDeadlock {
    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight(){
        synchronized (left){
            synchronized (right){
                doSomething();
            }
        }
    }
    public void rightLeft(){
        synchronized (right){
            synchronized (left){
                doSomethingElse();
            }
        }
    }
    void doSomething(){};
    void doSomethingElse(){};
}
