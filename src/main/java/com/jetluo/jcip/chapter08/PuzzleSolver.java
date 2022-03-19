package com.jetluo.jcip.chapter08;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName PuzzleSolver
 *   程序清单 8-18
 * @Description 在解决器中找不到解答
 * @Author jet
 * @Date 2022/3/18 22:46
 * @Version 1.0
 **/
public class PuzzleSolver<P,M> extends  ConcurrentPuzzleSolver<P,M>{

    public PuzzleSolver(Puzzle<P, M> puzzle) {
        super(puzzle);
    }

    private final AtomicInteger taskCount = new AtomicInteger(0);

    @Override
    protected Runnable newTask(P p, M m, PuzzleNode<P,M> n){
        return new CountingSolverTask(p, m, n);
    }
    class CountingSolverTask extends SolverTask{

        public CountingSolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
            taskCount.incrementAndGet();
        }

        @Override
        public void run() {
            try {
                super.run();
            }finally {
                if (taskCount.decrementAndGet() == 0){
                    solution.setValue(null);
                }
            }

        }
    }

}
