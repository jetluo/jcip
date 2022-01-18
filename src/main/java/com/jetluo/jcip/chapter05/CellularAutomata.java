package com.jetluo.jcip.chapter05;

import javafx.concurrent.Worker;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @ClassName CellularAutomata
 * @Description Coordinating computation in a cellular automaton with CyclicBarrier
 * @Author jet
 * @Date 2022/1/7 16:21
 * @Version 1.0
 **/
public class CellularAutomata {
    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    public CellularAutomata(Board board){
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count, new Runnable() {
            @Override
            public void run() {
                mainBoard.commitNewValues();
            }
        });
        this.workers = new Worker[count];
        for (int i=0 ; i< count;i++){
            workers[i]= new Worker(mainBoard.getSubBoard(count, i));
        }
    }
    private class Worker implements Runnable{
        private final  Board board;
        public Worker(Board board){
            this.board = board;
        }
        @Override
        public  void run(){
            while (!board.hasConverged()){
                for (int x = 0; x<board.getMaxX();x++){
                    for (int y = 0; y < board.getMaxY(); y++) {
                        board.setNewValue(x, y, computeValue(x, y));
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
        private int computeValue(int x,int y){
            return  0;
        }
    }

    public void start(){
        for (int i = 0; i < workers.length; i++) {
            new Thread(workers[i]).start();
        }
        mainBoard.waitForConvergence();
    }

    interface Board{
        int getMaxX();
        int getMaxY();
        int getValue(int x,int y);
        int setNewValue(int x,int y,int value);
        void commitNewValues();
        boolean hasConverged();
        void waitForConvergence();
        Board getSubBoard(int numPartitions,int index);
    }
}
