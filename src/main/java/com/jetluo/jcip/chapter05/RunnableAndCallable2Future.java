package com.jetluo.jcip.chapter05;

import java.util.concurrent.*;

/**
 * @ClassName RunnableAndCallable2Future
 * @Description TODO
 * @Author jet
 * @Date 2022/1/11 11:00
 * @Version 1.0
 **/
public class RunnableAndCallable2Future {
    public static void main(String[] args) {
        // 创建一个执行任务的服务
        ExecutorService executorService= Executors.newFixedThreadPool(3);

        try {

            //1。Runnable通过Future返回结果为空
            // 创建Runnable，来调度，等待任务执行完毕，取得返回结果。
            Future<?> runnable1 = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("runnable1 running. ");
                }
            });
            System.out.println("runnable1:" + runnable1.get());
            // 2.Callable通过Future能返回结果
            // 提交并执行任务，任务启动时返回了一个Future对象，
            // 如果想得到任务执行的结果或者是异常可对这个Future对象进行操作
            Future<String> future1 = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "result=future1";
                }
            });
            System.out.println("future1:" + future1.get());
            //3. 对Callable调用cancel可以对该任务进行中断
            // 提交并执行任务，任务启动时返回了一个Future对象，
            // 如果想得到任务执行的结果或者是异常可对这个Future对象进行操作
            Future<String> future2 = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    try {
                        while (true) {
                            System.out.println("future2 running.");
                            Thread.sleep(50);
                        }
                    }catch (InterruptedException e){
                        System.out.println("InterruptedException future2");
                    }
                    return "result=future2";
                }
            });
            Thread.sleep(10);
            System.out.println("future2 cancel:" + future2.cancel(true));
            //4.用Callable时抛出异常则Future什么也取不到了
            //获取第三个任务的输出，因为执行第三个任务会引起异常
            //所以下面的语句将引起异常的抛出
            Future<String> future3 = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                  //  return "future3 throws Exception";
                   throw  new InterruptedException("future3 throws Exception !");
                }
            });
            System.out.println("future3 throws Exception:"+future3.get());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // 停止任务执行服务
        executorService.shutdownNow();
    }
}
