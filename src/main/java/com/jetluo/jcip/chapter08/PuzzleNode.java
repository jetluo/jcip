package com.jetluo.jcip.chapter08;

import net.jcip.annotations.Immutable;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName PuzzleNode
 * 程序清单 8-14
 * @Description 用于谜题解决框架的链表节点
 *    PuzzleNode代表通过一系列的移动到达的一个位置，其中保存了到达该位置的移动以及前一个PuzzleNode
 *    只要沿着PuzzleNode链接逐步回溯，就可以重新构建出到达当前位置的移动序列。
 * @Author jet
 * @Date 2022/3/16 22:53
 * @Version 1.0
 **/
@Immutable
public class PuzzleNode<P,M> {
    final P pos;
    final M move;
    final PuzzleNode<P,M> prev;

    public PuzzleNode(P pos,M move,PuzzleNode<P,M> prev){
        this.pos = pos;
        this.move = move;
        this.prev = prev;
    }
    List<M> asMoveList(){
        List<M> solution = new LinkedList<M>();
        for (PuzzleNode<P,M> n = this; n.move != null;n = n.prev){
            System.out.println("PuzzleNode:" + n.pos);
            solution.add(0,n.move);
        }
        return solution;
    }
}
