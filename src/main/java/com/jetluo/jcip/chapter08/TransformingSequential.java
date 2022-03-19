package com.jetluo.jcip.chapter08;


import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @ClassName TransformingSequential
 *   程序清单 8-10
 * @Description 将串行执行替换为并行执行
 * @Author jet
 * @Date 2022/3/14 22:45
 * @Version 1.0
 **/
public abstract class TransformingSequential {
    /**
     * @Author jet
     * @Description //串行计算方式
     * @Date 2022/3/16
     * @Param [elements]
     * @return void
     **/
    void processSequentially(List<Element> elements){
        for (final  Element e:elements) {
            process(e);
        }
    }
    /**
     * @Author jet
     * @Description //并行计算方式
     * @Date 2022/3/16
     * @Param [exec, elements]
     * @return void
     **/
    void processInParallel(Executor exec,List<Element> elements){
        for (final Element e:elements){
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    process(e);
                }
            });
        }
    }
    /**
     * @Author jet
     * @Description ////串行计算方式
     * @Date 2022/3/16
     * @Param [nodes, results]
     * @return void
     **/
    public <T> void sequentialRecursive(List<Node<T>> nodes, Collection<T> results){
        for (Node<T> n : nodes){
            results.add(n.compute());
            sequentialRecursive(n.getChildren(), results);
        }
    }
    /**
     * @Author jet
     * @Description //并行计算方式
     * @Date 2022/3/16
     * @Param [exec, nodes, results]
     * @return void
     **/
    public <T> void parallelRecursive(final Executor exec,List<Node<T>> nodes,final Collection<T> results){
        for (final Node<T> n:nodes){
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    results.add(n.compute());
                }
            });
            parallelRecursive(exec, n.getChildren(), results);
        }
    }

    public <T> Collection<T> getParallelResults(List<Node<T>> nodes) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Queue<T> resultQueue = new ConcurrentLinkedQueue<T>();
        parallelRecursive(exec, nodes, resultQueue);
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        return resultQueue;

    }

    public abstract void process(Element e);

    interface Element {}

    interface Node<T>{
        T compute();
        List<Node<T>> getChildren();
    }
}
