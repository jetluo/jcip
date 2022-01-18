package com.jetluo.jcip.chapter06;

import java.util.Timer;
import java.util.TimerTask;
import  static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @ClassName OutOfTime
 * @Description 错误的Timer行为
 * @Author jet
 * @Date 2022/1/14 14:43
 * @Version 1.0
 **/
public class OutOfTime {
    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(), 1);
        SECONDS.sleep(1);
        timer.schedule(new ThrowTask(), 1);
        SECONDS.sleep(5);
    }
    static class  ThrowTask extends TimerTask{
        @Override
        public void run() {
            throw  new RuntimeException();
        }
    }
}
