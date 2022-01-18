package com.jetluo.jcip.chapter05;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @ClassName Memoizer3
 * @Description TODO
 * @Author jet
 * @Date 2022/1/10 22:42
 * @Version 1.0
 **/
public class Memoizer3 <A,V> implements Computable<A,V>{
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<A,Future<V>>();
    private final  Computable<A,V> c;

    public  Memoizer3(Computable<A,V> c){
        this.c = c;
    }
    @Override
    public V compute(final  A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        if (f == null){
            Callable<V> eval = new Callable<V>() {
                @Override
                public V call() throws Exception {
                        return  c.compute(arg);
                }
            };
            FutureTask<V> ft = new FutureTask<V>(eval);
            f = ft;
            cache.put(arg, ft);
            ft.run();
        }
        try {
            return f.get();
        } catch (ExecutionException e) {
            throw  LaunderThrowable.launderThrowable(e.getCause());
        }
    }

}
