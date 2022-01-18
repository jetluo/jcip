package com.jetluo.jcip.chapter05;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @ClassName Solver
 * @Description TODO
 * @Author jet
 * @Date 2022/1/10 10:28
 * @Version 1.0
 **/
public class Solver {
    final int N;
    final float[][] data;
    final CyclicBarrier barrier;

    class Worker implements  Runnable{
        int myRow;
        Worker(int row){
            myRow = row;
        }
        @Override
        public void run() {
           // processRow(myRow);
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
    public Solver(float[][] matrix){
        data = matrix;
        N = matrix.length;
        Runnable barrierAction = new Runnable() {
            @Override
            public void run() {
            //    mergeRows();
            }
        };
        barrier = new CyclicBarrier(N, barrierAction);
        List<Thread> threads = new ArrayList<Thread>(N);
        for (int i = 0; i < N; i++) {
            Thread thread = new Thread(new Worker(i));
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
