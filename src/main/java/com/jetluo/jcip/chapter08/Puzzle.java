package com.jetluo.jcip.chapter08;

import java.util.Set;

/**
 * @ClassName Puzzle
 *  程序清单 8-13
 * @Description 表示搬箱子之类谜题的抽象类 P表示位置类，M表示移动类
 * @Author jet
 * @Date 2022/3/16 22:48
 * @Version 1.0
 **/
public interface Puzzle <P,M>{
    /**
     * @Author jet
     * @Description 初始位置
     * @Date 2022/3/18
     * @Param []
     * @return P
     **/
    P initialPosition();
    /**
     * @Author jet
     * @Description //是否目标位置
     * @Date 2022/3/18
     * @Param [position]
     * @return boolean
     **/
    boolean isGola(P position);
    /**
     * @Author jet
     * @Description 有效移动的规则集
     * @Date 2022/3/18
     * @Param [position]
     * @return java.util.Set<M>
     **/
    Set<M> legalMoves(P position);
    /**
     * @Author jet
     * @Description //修改移动位置
     * @Date 2022/3/18
     * @Param [position, move]
     * @return P
     **/
    P move(P position,M move);
}
