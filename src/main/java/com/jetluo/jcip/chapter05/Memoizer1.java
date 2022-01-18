package com.jetluo.jcip.chapter05;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Memoizer1
 * @Description TODO
 * @Author jet
 * @Date 2022/1/10 22:14
 * @Version 1.0
 **/
public class Memoizer1 <A,V> implements Computable<A,V>{
    private  final Map<A,V> cache = new HashMap<A,V>();
    private final Computable<A,V> c;

    public Memoizer1(Computable<A,V> c){
        this.c =c;
    }

    @Override
    public synchronized V compute(A arg)throws InterruptedException{
        V result = cache.get(arg);
        if (result == null){
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}

interface Computable<A,V>{
    V compute(A arg)throws InterruptedException;
}

class  ExpensiveFunction implements Computable<String, BigInteger>{

    @Override
    public BigInteger compute(String arg) throws InterruptedException {

        // fater deep thought...
        return new BigInteger(arg);
    }
}
