package com.jetluo.jcip.chapter07;

import net.jcip.annotations.GuardedBy;


import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName WebCrawler
 * @Description 7-22 使用TrackingExecutor 来保存未完成的任务以备后续执行。
 * @Author jet
 * @Date 2022/3/2 22:05
 * @Version 1.0
 **/
public abstract class WebCrawler {
    private volatile TrackingExecutor exec;
    @GuardedBy("this")
    private final Set<URL> urlsToCrawl = new HashSet<URL>();
    private final ConcurrentMap<URL,Boolean> seen = new ConcurrentHashMap<URL, Boolean>();
    private static final long TIMEOUT = 500;
    private static final TimeUnit UNIT = TimeUnit.MICROSECONDS;

    public WebCrawler(URL startUrl){
        urlsToCrawl.add(startUrl);
    }
    /**
     * @Author jet
     * @Description 开始任务并提交任务，清理集合
     * @Date 2022/3/2
     * @Param []
     * @return void
     **/
    public synchronized void start(){
        exec = new TrackingExecutor(Executors.newCachedThreadPool());
        for (URL url :urlsToCrawl){
            submitCrawlTask(url);
        }
        urlsToCrawl.clear();
    }
    /**
     * @Author jet
     * @Description 停止线程池任务并保存线程池剩余任务
     * @Date 2022/3/2
     * @Param []
     * @return void
     **/
    public synchronized void stop() throws InterruptedException{
        try {
            saveUncrawled(exec.shutdownNow());
            if (exec.awaitTermination(TIMEOUT, UNIT)){
                saveUncrawled(exec.getCancelledTasks());
            }
        }finally {
            exec = null;
        }
    }
    /**
     * @Author jet
     * @Description 处理每个页面
     * @Date 2022/3/2
     * @Param [url]
     * @return java.util.List<java.net.URL>
     **/
    protected abstract List<URL> processPage(URL url);
    /**
     * @Author jet
     * @Description 存储在任务取消后当线程池里未执行的剩余任务
     * @Date 2022/3/2
     * @Param [uncrawled]
     * @return void
     **/
    private void saveUncrawled(List<Runnable> uncrawled){
        for (Runnable task:uncrawled){
            urlsToCrawl.add( ((CrawlTask)task).getPage());
        }
    }
    /**
     * @Author jet
     * @Description 提交爬虫任务给线程池执行
     * @Date 2022/3/2
     * @Param [u]
     * @return void
     **/
    private void submitCrawlTask(URL u){
        exec.execute( new CrawlTask(u));
    }
    private class CrawlTask implements Runnable{
        private final  URL url;

        CrawlTask(URL url){
            this.url = url;
        }

        private  int count = 1;

        boolean alreadyCrawled(){
            return seen.putIfAbsent(url, true) != null;
        }

        void markUncrawled(){
            seen.remove(url);
            System.out.printf("marking %s uncrawled %n",url);
        }

        @Override
        public void run() {
            for (URL link :processPage(url)){
                if (Thread.currentThread().isInterrupted()){
                    return;
                }
                submitCrawlTask(url);
            }
        }
        public URL getPage(){
            return url;
        }
    }

}
