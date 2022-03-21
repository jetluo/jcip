package com.jetluo.jcip.chapter10;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName DynamicOrderDeadlock
 *   程序清单 10-2
 * @Description 动态的锁顺序死锁（不要这么做）
 * @Author jet
 * @Date 2022/3/19 22:54
 * @Version 1.0
 **/
public class DynamicOrderDeadlock {
    // Warning: deadlock-prone!
    public static void transferMoney(Account fromAccount,
                                     Account toAccount,
                                     DollarAmount amount) throws InsufficientFundsException {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException();
                } else {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }
    }


    static class DollarAmount implements Comparable<DollarAmount> {
        // Needs implementation
        public DollarAmount(int amount) {

        }

        public DollarAmount add(DollarAmount d) {
            return null;
        }

        public DollarAmount subtract(DollarAmount d) {
            return null;
        }

        @Override
        public int compareTo(DollarAmount dollarAmount) {
            return 0;
        }
    }

    static class InsufficientFundsException extends Exception {

    }

    static class Account {
        private DollarAmount balance;
        private final int accNo;

        private static final AtomicInteger sequence = new AtomicInteger();

        public Account() {
            accNo = sequence.incrementAndGet();
        }
        /**
         * @Author jet
         * @Description 减法
         * @Date 2022/3/20
         * @Param [d]
         * @return void
         **/
        void debit(DollarAmount d) {
            balance = balance.subtract(d);
        }
        /**
         * @Author jet
         * @Description 加法
         * @Date 2022/3/20
         * @Param [d]
         * @return void
         **/
        void credit(DollarAmount d) {
            balance = balance.add(d);
        }

        DollarAmount getBalance() {
            return balance;
        }

        int getAccNo() {
            return accNo;
        }

    }
}
