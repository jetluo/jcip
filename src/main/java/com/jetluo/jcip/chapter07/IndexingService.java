package com.jetluo.jcip.chapter07;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @ClassName IndexingService
 * @Description 通过"毒丸"对象来关闭服务
 * 适用于在生产者和消费者的数量都已知的情况，才可以使用"毒丸"。
 * @Author jet
 * @Date 2022/2/17 10:45
 * @Version 1.0
 **/
public class IndexingService {
    private static final int CAPACITY = 1000;
    private static final File POISON = new File("");
    private final IndexerThread consumer = new IndexerThread();
    private final CrawlerThread producer = new CrawlerThread();
    private final BlockingQueue<File> queue;
    private final FileFilter fileFilter;
    private final File root;

    public IndexingService(File root,final FileFilter fileFilter){
        this.root = root;
        this.queue = new LinkedBlockingQueue<File>(CAPACITY);
        this.fileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory()|| fileFilter.accept(f);
            }
        };
    }
    private boolean alreadyIndexed(File f){
        return false;
    }

    class CrawlerThread extends  Thread{
        @Override
        public void run() {
            try{
                crawl(root);
            }catch (InterruptedException e){

            }finally {
                while (true){
                    try{
                        queue.put(POISON);
                        break;
                    }catch (InterruptedException ex){

                    }
                }
            }
        }

        private  void crawl(File root)throws  InterruptedException{
            File[] entries = root.listFiles(fileFilter);
            if (entries!=null){
                for (File entry : entries){
                    if (entry.isDirectory()){
                        crawl(entry);
                    }else if(!alreadyIndexed(entry)) {
                        queue.put(entry);
                    }
                }
            }
        }
    }

    class IndexerThread extends  Thread{
        @Override
        public void run() {
            try {
                while (true){
                    File file = queue.take();
                    if (file == POISON){
                        break;
                    }else {
                        indexFile(file);
                    }
                }
            }catch (InterruptedException consumed){

            }
        }
        public void indexFile(File file){

        }
    }

    public void start(){
        producer.start();
        consumer.start();
    }

    public void stop(){
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException{
        consumer.join();
    }


}
