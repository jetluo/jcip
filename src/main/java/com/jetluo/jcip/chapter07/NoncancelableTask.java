package com.jetluo.jcip.chapter07;


import java.util.concurrent.BlockingQueue;

/**
 * @ClassName NoncancelableTask
 * @Description 不可取消的任务在退出前恢复中断
 * @Author jet
 * @Date 2022/1/20 15:21
 * @Version 1.0
 **/
public class NoncancelableTask {
    public Task getNexttask(BlockingQueue<Task> queue) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    interface Task {
    }
}
