package com.jetluo.jcip.chapter07;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @ClassName UEHLogger
 * @Description 将异常写入日志的UncaughtExceptionHandler
 * @Author jet
 * @Date 2022/3/3 15:44
 * @Version 1.0
 **/
public class UEHLogger implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger logger = Logger.getAnonymousLogger();
        logger.log(Level.SEVERE,"Thread terminated with exeception: " + t.getName(),e);
    }
}
