package com.jetluo.jcip.chapter08;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName SequentialPuzzleSoler
 *  程序清单 8-15
 * @Description 串行的谜题解答器
 *  他在谜题空间中执行一个深度优先搜索，当找到解答方案（不一定是最短的解决方案）后结束搜索。
 * @Author jet
 * @Date 2022/3/16 22:59
 * @Version 1.0
 **/
public class SequentialPuzzleSoler <P,M>{
    private final Puzzle<P,M> puzzle;
    private final Set<P> seen = new HashSet<P>();

    public SequentialPuzzleSoler(Puzzle<P,M> puzzle){
        this.puzzle = puzzle;
    }

    public List<M> solve(){
        P pos = puzzle.initialPosition();
        return search(new PuzzleNode<P,M>(pos,null,null));
    }
    private List<M> search(PuzzleNode<P,M> node){
        if (!seen.contains(node.pos)){
            seen.add(node.pos);
            if (puzzle.isGola(node.pos)){
                return node.asMoveList();
            }
            for (M move:puzzle.legalMoves(node.pos)){
                P pos = puzzle.move(node.pos, move);
                PuzzleNode<P,M> child = new PuzzleNode<P,M>(pos, move, node);
                List<M> result = search(child);
                if (result !=null)
                {
                    return result;
                }
            }
        }
        return null;
    }

}
