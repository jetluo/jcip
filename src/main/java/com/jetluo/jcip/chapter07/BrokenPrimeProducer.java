package com.jetluo.jcip.chapter07;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * @ClassName BrokenPrimeProducer
 * @Description TODO
 * @Author jet
 * @Date 2022/1/20 13:51
 * @Version 1.0
 **/
public class BrokenPrimeProducer extends Thread{
    private final BlockingQueue<BigInteger> queue;
    private volatile boolean cancelled = false;

    BrokenPrimeProducer(BlockingQueue<BigInteger> queue){
        this.queue = queue;
    }
    @Override
    public void run(){

        try {
            BigInteger p = BigInteger.ONE;
            while (!cancelled){
                queue.put(p =p.nextProbablePrime());
            }
        }catch (InterruptedException consumed){

        }
    }
    public void cancel (){
        cancelled =true;
    }
}
