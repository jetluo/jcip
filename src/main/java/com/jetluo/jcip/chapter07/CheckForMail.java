package com.jetluo.jcip.chapter07;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName CheckForMail
 * @Description 使用私有的Executor，并且该Executor的生命周期受限于方法调用
 * @Author jet
 * @Date 2022/3/2 10:15
 * @Version 1.0
 **/
public class CheckForMail {
    boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        final AtomicBoolean hasNewMail = new AtomicBoolean(false);
        try {
            for (final String host : hosts) {
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (checkMail(host)) {
                            hasNewMail.set(true);
                        }
                    }
                });
            }
        } finally {
            exec.shutdown();
            exec.awaitTermination(timeout, unit);
        }
        return hasNewMail.get();
    }

    private boolean checkMail(String host) {
        System.out.println("host:"+host+"时间：" + System.currentTimeMillis());
        return false;
    }

    public static void main(String[] args) {
        CheckForMail checkForMail = new CheckForMail();
        Set<String> hosts = new HashSet<>();
        hosts.add("localhost");
        hosts.add("127.0.0.1");
        try {
            checkForMail.checkMail(hosts,1,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
