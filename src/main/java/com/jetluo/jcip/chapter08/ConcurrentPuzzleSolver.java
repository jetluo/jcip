package com.jetluo.jcip.chapter08;

import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName ConcurrentPuzzleSolver
 *  程序清单 8-16
 * @Description 并发的谜题解答器
 * 使用了内部类 SolverTask,这个类扩展了Node并实现了Runnable。
 * 首先计算出下一步可能到达的所有位置，并去掉已经到达的位置，然后判断是否已经成功地完成。
 * 最后将尚未搜索过的位置提交给Executor
 * @Author jet
 * @Date 2022/3/17 22:32
 * @Version 1.0
 **/
public class ConcurrentPuzzleSolver<P,M> {
    private final Puzzle<P,M> puzzle;

    private final ExecutorService exec;

    private final ConcurrentMap<P,Boolean> seen;

    protected final  ValueLatch<PuzzleNode<P,M>> solution = new ValueLatch<PuzzleNode<P,M>>();

    public ConcurrentPuzzleSolver(Puzzle<P,M> puzzle){
        this.puzzle = puzzle;
        this.exec = initThreadPool();
        this.seen = new ConcurrentHashMap<P,Boolean>();
        if (exec instanceof ThreadPoolExecutor){
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
            // 用于被拒绝任务的处理程序，默认情况下它将丢弃被拒绝的任务
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }

    }

    private ExecutorService initThreadPool(){
       return   Executors.newCachedThreadPool();
    }

    public List<M> solve() throws InterruptedException{
        try{
            P p = puzzle.initialPosition();
            exec.execute(newTask(p,null,null));
            // block until solution found 阻塞直到找到解答
            PuzzleNode<P,M> solnPuzzleNode = solution.getValue();
            return (solnPuzzleNode == null)?null:solnPuzzleNode.asMoveList();
        }finally {
            exec.shutdown();
        }
    }

    protected Runnable newTask(P p,M m,PuzzleNode<P,M> n){
        return new SolverTask(p,m,n);
    }
    protected class SolverTask extends PuzzleNode<P,M> implements Runnable{

        public SolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
        }

        @Override
        public void run() {
            if (solution.isSet() || seen.putIfAbsent(pos, true)!= null){
                // 已经找到了解答或者已经遍历了这个位置
                return; // already solved or seen this position
            }
            if (puzzle.isGola(pos)){
                solution.setValue(this);
            }else{
                for (M m :puzzle.legalMoves(pos)){
                    exec.execute(newTask(puzzle.move(pos, m), m, this));
                }
            }

        }
    }


}
