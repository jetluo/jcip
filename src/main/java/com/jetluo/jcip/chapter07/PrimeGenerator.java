package com.jetluo.jcip.chapter07;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import  static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @ClassName Primegenerator
 * @Description 使用volatile类型的域来保存取消状态
 * @Author jet
 * @Date 2022/1/20 10:23
 * @Version 1.0
 **/
@ThreadSafe
public class PrimeGenerator implements Runnable{
    private  static ExecutorService exec =  Executors.newCachedThreadPool();

    @GuardedBy("this")
    private final List<BigInteger> primes = new ArrayList<BigInteger>();

    private  volatile boolean cancelled ;
    @Override
    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!cancelled){
            p = p.nextProbablePrime();
            synchronized (this){
                primes.add(p);
            }
        }
    }

    public  void cancel(){
        cancelled =true;
    }
    public synchronized List<BigInteger> get(){
        return new ArrayList<BigInteger>(primes);
    }
    static List<BigInteger> aSecondOfPrimes() throws  InterruptedException{
        PrimeGenerator primegenerator = new PrimeGenerator();
        exec.execute(primegenerator);
        try {
            SECONDS.sleep(1);
        }finally {
            primegenerator.cancel();
        }
        return primegenerator.get();
    }

    public static void main(String[] args) {
        try {
         List<BigInteger> pri =   aSecondOfPrimes();
            for (BigInteger bi: pri)  {
                System.out.println("数据："+bi);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
