package com.jetluo.jcip.chapter07;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * @ClassName PrimeProducer
 * @Description 通过中断来取消线程任务
 * @Author jet
 * @Date 2022/1/20 14:17
 * @Version 1.0
 **/
public class PrimeProducer extends Thread{
    private  final BlockingQueue<BigInteger> queue;

    PrimeProducer(BlockingQueue<BigInteger> queue){
        this.queue =queue;
    }
    @Override
    public void run() {
       try {
           BigInteger p = BigInteger.ONE;
           while (!Thread.currentThread().isInterrupted()){
               queue.put(p = p.nextProbablePrime());
           }
       }catch (InterruptedException consumed){
           // allow thread to exit
       }
    }
    public void cancel(){
        // 父类线程调用
        interrupt();
    }
}
