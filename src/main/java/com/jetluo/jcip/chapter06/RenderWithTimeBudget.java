package com.jetluo.jcip.chapter06;

import java.util.concurrent.*;

/**
 * @ClassName RenderWithTimeBudget
 * @Description TODO
 * @Author jet
 * @Date 2022/1/18 14:55
 * @Version 1.0
 **/
public class RenderWithTimeBudget {
    private static  final Ad DEFAULT_AD = new Ad();
    private static final long TIME_BUDGET = 1000;
    private static  final ExecutorService exec = Executors.newCachedThreadPool();

    Page renderPageWithAd() throws  InterruptedException{

        long endNanos = System.nanoTime() + TIME_BUDGET;
        Future<Ad> f = exec.submit(new FetchAdTask());
        Page page = renderPageBody();

        Ad ad;
        try {
            long timeLeft = endNanos - System.nanoTime();
            // timeLeft这个时间点可能是负数，但java线程中当作0处理，无需额外处理。
             ad = f.get(timeLeft, TimeUnit.NANOSECONDS);
        } catch (ExecutionException e) {
           ad = DEFAULT_AD;
        } catch (TimeoutException e) {
            ad = DEFAULT_AD;
            f.cancel(true);
        }
        page.setAd(ad);
        return  page;
    }
    Page renderPageBody(){
        return  new Page();
    }
    static class  Ad{

    }
    static class  Page{
        public void setAd(Ad ad){

        }
    }
    static  class FetchAdTask implements Callable<Ad>{

        @Override
        public Ad call() throws Exception {
            return new Ad();
        }
    }
}
