package com.jetluo.jcip.chapter05;


import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ProducerConsumer
 * <p/>
 * Producer and consumer tasks in a desktop search application
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ProducerConsumer {
    /**
     * @Author jet
     * @Description 生产者任务，即在某个文件层次结构中搜索符合索引标准的文件，并将它们的名称放入工作队列。
     * @Date 2021/12/30
     * @Param
     * @return
     **/
    static class FileCrawler implements Runnable {
        private final BlockingQueue<File> fileQueue;
        private final FileFilter fileFilter;
        private final File root;

        public FileCrawler(BlockingQueue<File> fileQueue,
                           final FileFilter fileFilter,
                           File root) {
            this.fileQueue = fileQueue;
            this.root = root;
            this.fileFilter = new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || fileFilter.accept(f);
                }
            };
        }

        private boolean alreadyIndexed(File f) {
            return false;
        }

        @Override
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void crawl(File root) throws InterruptedException {
            File[] entries = root.listFiles(fileFilter);
            if (entries != null) {
                for (File entry : entries) {
                    if (entry.isDirectory()) {
                        crawl(entry);
                    } else if (!alreadyIndexed(entry)) {
                        System.out.println("生产文件："+ entry.getName());
                        fileQueue.put(entry);
                    }
                }
            }
        }
    }

    /**
     * @Author jet
     * @Description 消费者任务，即从队列中取出文件名称并对它们建立索引。
     * @Date 2021/12/30
     * @Param
     * @return
     **/
    static class Indexer implements Runnable {
        private final BlockingQueue<File> queue;

        public Indexer(BlockingQueue<File> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    indexFile(queue.take());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void indexFile(File file) {
            // Index the file...
            System.out.println("建立索引："+file.getName());
        }

        ;
    }

    private static final int BOUND = 10;
    private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();

    public static void startIndexing(File[] roots) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<File>(BOUND);
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return true;
            }
        };
        // 生产
        for (File root : roots) {
            new Thread(new FileCrawler(queue, filter, root)).start();
        }
        // 消费
        for (int i = 0; i < N_CONSUMERS; i++) {
            new Thread(new Indexer(queue)).start();
        }
    }

    public static void main(String[] args) {
        // 本程序的缺点是永不退出
        System.out.println("虚拟机可处理数："+Runtime.getRuntime().availableProcessors());
        File[] roots= new File[2];
        roots[0] = new File("/Users/jet/Downloads/考核表");
        roots[1] = new File("/Users/jet/Downloads/阳光电影www.ygdy8.com.新生化危机.2021.BD.1080P.中英双字.mkv/");
        ProducerConsumer.startIndexing(roots);
    }
}