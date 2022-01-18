package com.jetluo.jcip.chapter06;

import java.util.List;
import java.util.concurrent.*;
import static com.jetluo.jcip.chapter05.LaunderThrowable.launderThrowable;

/**
 * @ClassName Renderer
 * @Description 使用CompletionService使页面元素在下载完成后立即显示出来。
 * @Author jet
 * @Date 2022/1/18 14:07
 * @Version 1.0
 **/
public abstract class Renderer {
    private final ExecutorService executor;

    Renderer(ExecutorService executor) {
        this.executor = executor;
    }

    void renderPage(CharSequence source) {
        final List<ImageInfo> info = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new ExecutorCompletionService<ImageData>(executor);
        for (final ImageInfo imageInfo : info) {
            completionService.submit(new Callable<ImageData>() {
                @Override
                public ImageData call() throws Exception {
                    return imageInfo.downloadImage();
                }
            });
            renderText(source);
            try {
                for (int i = 0; i < info.size(); i++) {
                    Future<ImageData> f = completionService.take();

                    ImageData imageData = f.get();

                    renderImage(imageData);

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                throw launderThrowable(e.getCause());
            }

        }

    }

    interface ImageData {

    }

    interface ImageInfo {
        ImageData downloadImage();
    }

    abstract void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    abstract void renderImage(ImageData i);
}
