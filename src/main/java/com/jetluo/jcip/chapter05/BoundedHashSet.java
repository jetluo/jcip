package com.jetluo.jcip.chapter05;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * @ClassName BoundedHashSet
 * @Description TODO
 * @Author jet
 * @Date 2022/1/6 16:10
 * @Version 1.0
 **/
public class BoundedHashSet <T>{

    private final Set<T> set;

    private final Semaphore semaphore;

    public BoundedHashSet(int bound){
        this.set = Collections.synchronizedSet(new HashSet<T>());
        semaphore = new Semaphore(bound);
    }

    public boolean add(T o)throws InterruptedException{
        semaphore.acquire();
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return  wasAdded;
        }finally {
            if (!wasAdded ){
                semaphore.release();
            }
        }
    }

    public boolean remove(Object o){
        boolean wasRemoved = set.remove(o);
        if (wasRemoved){
            semaphore.release();
        }
        return  wasRemoved;
    }

    public static void main(String[] args) {
        BoundedHashSet boundedHashSet= new BoundedHashSet<String>(10);
        try {

            System.out.println(boundedHashSet.add(1));
            System.out.println(boundedHashSet.add(2));
            System.out.println(boundedHashSet.semaphore.availablePermits());
            System.out.println(boundedHashSet.remove(1));
            System.out.println(boundedHashSet.semaphore.availablePermits());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
