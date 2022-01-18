package com.jetluo.jcip.chapter06;


import static com.jetluo.jcip.chapter05.LaunderThrowable.launderThrowable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName FutureRenderer
 * @Description 使用Future等待图像下载
 * @Author jet
 * @Date 2022/1/17 15:55
 * @Version 1.0
 **/
public abstract class  FutureRenderer {
    private final ExecutorService executor= Executors.newCachedThreadPool();

    void renderPage(CharSequence source){
        // 获取图片信息
        final  List<ImageInfo> imageInfos =scanForImageInfo(source);
        // 定制图片下载任务
        Callable<List<ImageData>> task = new Callable<List<ImageData>>() {
            @Override
            public List<ImageData> call() throws Exception {
                List<ImageData> result = new ArrayList<ImageData>();
                for (ImageInfo imageInfo:imageInfos) {
                    result.add(imageInfo.downloadImage());
                }
                return result;
            }
        };
        //并发任务1：图片下载
        Future<List<ImageData>> future = executor.submit(task);
        //并发任务2：文本渲染
        renderText(source);
        try {
            // 获取图片下载情况
            List<ImageData> imageData = future.get();
            for (ImageData data:imageData){
                // 渲染图片
                renderImage(data);
            }
        } catch (InterruptedException e) {
            // Re-assert the thread's interrupted status
            Thread.currentThread().interrupt();
            // We don't need the result ,so cancel the task too
            future.cancel(true);
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
        }
    }

   interface ImageData{

   }
    interface ImageInfo{
        ImageData downloadImage();
    }
    abstract  void renderText(CharSequence s);
    abstract  List<ImageInfo> scanForImageInfo(CharSequence s);
    abstract void renderImage(ImageData i);
}
