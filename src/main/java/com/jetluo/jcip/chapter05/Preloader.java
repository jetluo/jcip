package com.jetluo.jcip.chapter05;


import java.util.concurrent.*;

/**
 * Preloader
 * 预加载设计
 * Using FutureTask to preload data that is needed later
 *
 * @author Brian Goetz and Tim Peierls
 */

public class Preloader {
    ProductInfo loadProductInfo() throws DataLoadException {
        ProductInfo info=new   Products("names");
        return info;
    }

    private final FutureTask<ProductInfo> future =
            new FutureTask<ProductInfo>(new Callable<ProductInfo>() {
                @Override
                public ProductInfo call() throws DataLoadException {
                    return loadProductInfo();
                }
            });

    private final Thread thread = new Thread(future);

    public void start() { thread.start(); }

    public ProductInfo get()
            throws DataLoadException, InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException) {
                throw (DataLoadException) cause;
            }
            else {
                throw LaunderThrowable.launderThrowable(cause);
            }
        }
    }



    public static void main(String[] args) {
        Preloader preloader=new Preloader();
        try {
            preloader.start();
            System.out.println(  preloader.get().getNames());
        } catch (DataLoadException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
interface ProductInfo {
    String getNames();
}
class Products implements  ProductInfo{
    private String name;
    Products(String name){
        this.name = name;
    }
    @Override
    public String getNames() {
        return this.name;
    }
}
class DataLoadException extends Exception { }