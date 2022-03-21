package com.jetluo.jcip.chapter10;

import java.util.Random;
import  com.jetluo.jcip.chapter10.DynamicOrderDeadlock.Account;
import com.jetluo.jcip.chapter10.DynamicOrderDeadlock.DollarAmount;
/**
 * @ClassName DemonstrateDeadlock
 *  程序清单 10-4
 * @Description 在典型条件下发生死锁的循环
 * @Author jet
 * @Date 2022/3/20 10:08
 * @Version 1.0
 **/
public class DemonstrateDeadlock {
    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1000000;

    public static void main(String[] args) {
        final Random rnd = new Random();
        final Account[] accounts = new Account[NUM_ACCOUNTS];
        for (int i= 0;i < accounts.length;i++){
            accounts[i]= new Account();
        }
        class TransferThread extends Thread{
            @Override
            public void run() {
                for (int i = 0;i < NUM_ITERATIONS;i++){
                    int fromAcct = rnd.nextInt(NUM_ACCOUNTS);
                    int toAcct = rnd.nextInt(NUM_ACCOUNTS);
                    DollarAmount amount = new DollarAmount(rnd.nextInt(1000));

                    try{
                        DynamicOrderDeadlock.transferMoney(accounts[fromAcct], accounts[toAcct], amount);
                    }catch (DynamicOrderDeadlock.InsufficientFundsException ignored){

                    }
                }
            }
        }
        for (int i = 0;i < NUM_THREADS;i++){
            new TransferThread().start();
        }

    }

}
