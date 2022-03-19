package com.jetluo.jcip.chapter08;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @ClassName ThreadDeadlock
 * 程序清单 8-1
 * @Description 在单线程Executor中任务发生死锁 （不推荐）
 * @Author jet
 * @Date 2022/3/4 11:24
 * @Version 1.0
 **/
public class ThreadDeadlock {
    ExecutorService exec = Executors.newSingleThreadExecutor();

    public class LoadFileTask implements Callable<String> {
        private final String fileName;

        public LoadFileTask(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public String call() throws Exception {
            // 读取文件操作
            return "";
        }
    }

    public class RenderPageTask implements Callable<String> {

        private String renderBody() {
            //页面渲染
            return "";
        }

        @Override
        public String call() throws Exception {
            Future<String> header,footer;
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();
            // 将产生死锁 -- 任务等待子任务的完成。
            return header.get()+page + footer.get();

        }
    }
}
