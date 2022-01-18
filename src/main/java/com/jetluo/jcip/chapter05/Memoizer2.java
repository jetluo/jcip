package com.jetluo.jcip.chapter05;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName Memoizer2
 * @Description TODO
 * @Author jet
 * @Date 2022/1/10 22:33
 * @Version 1.0
 **/
public class Memoizer2 <A,V> implements  Computable<A,V>{
    private  final Map<A,V> cache = new ConcurrentHashMap<A,V>();
    private  final Computable<A,V> c;

    public  Memoizer2(Computable<A,V> c){
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null){
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
