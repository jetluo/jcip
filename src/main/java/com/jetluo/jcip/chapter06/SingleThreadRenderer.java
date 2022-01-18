package com.jetluo.jcip.chapter06;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SingleThreadRenderer
 * @Description 串行地渲染页面元素
 * @Author jet
 * @Date 2022/1/15 23:16
 * @Version 1.0
 **/
public abstract class SingleThreadRenderer {
    void renderPage(CharSequence source){
        renderText(source);
        List<ImageData> imageData = new ArrayList<ImageData>();
        for (ImageInfo imageInfo:scanForImageInfo(source)){
            imageData.add(imageInfo.downloadImage());
        }
        for(ImageData data:imageData){
            renderImage(data);
        }
    }
    interface  ImageData{}
    interface  ImageInfo{
        ImageData downloadImage();
    }
    abstract  void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    abstract  void renderImage(ImageData i);

}
