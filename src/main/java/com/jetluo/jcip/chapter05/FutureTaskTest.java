package com.jetluo.jcip.chapter05;

import java.util.concurrent.*;

/**
 * @ClassName FutureTaskTest
 * @Description TODO
 * @Author jet
 * @Date 2022/1/11 15:06
 * @Version 1.0
 **/
public class FutureTaskTest {
    public static void main(String[] args) {

        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("Sleep start.");
                Thread.sleep(1000*10);
                System.out.println("Sleep end.");
                return "time=" + System.currentTimeMillis();
            }
        };

        //直接用Thread的方式执行
        FutureTask<String> ft = new FutureTask<>(task);
        Thread t = new Thread(ft);
        t.start();
        try {
            System.out.println("waiting execute result");
            System.out.println("result = " + ft.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //使用Executors来执行
        System.out.println("ft2=======");
        FutureTask<String> ft2 = new FutureTask<>(task);
        // ft2.run();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(ft2);
        try {
            System.out.println("ft2 waiting execute result");
            System.out.println("ft2 result = " + ft2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

    }
}
