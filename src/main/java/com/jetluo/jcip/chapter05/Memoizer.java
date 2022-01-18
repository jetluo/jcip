package com.jetluo.jcip.chapter05;

import java.util.concurrent.*;

/**
 * @ClassName Memoizer
 * @Description 计算的高并发实现结果。
 * 此设计未能解决缓存逾期的问题，同样他也没有解决缓存清理的问题，即移除旧的计算结果以便为新的计算
 * 结果腾出空间，从而使缓存不会消耗过多的内存。
 * @Author jet
 * @Date 2022/1/11 15:53
 * @Version 1.0
 **/
public class Memoizer<A, V> implements Computable<A, V> {

    private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();

    private final Computable<A, V> c;

    public Memoizer(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(final A arg) throws InterruptedException {
        while (true) {
            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable<V> eval = new Callable<V>() {
                    @Override
                    public V call() throws InterruptedException {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<>(eval);
                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg, f);
            } catch (ExecutionException e) {
                throw LaunderThrowable.launderThrowable(e.getCause());
            }
        }
    }
}
