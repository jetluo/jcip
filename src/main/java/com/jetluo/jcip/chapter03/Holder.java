package com.jetluo.jcip.chapter03;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Holder
 * <p/>
 * Class at risk of failure if not properly published
 *
 * @author Brian Goetz and Tim Peierls
 */
public class Holder {
   public static Map<String,Date> lastLogin = Collections.synchronizedMap(new HashMap<String, Date>());

    public static void main(String[] args) {

        lastLogin.put("1", new Date());
        lastLogin.put("2", new Date());

        System.out.println(lastLogin.size());
    }
    private int n;

    public Holder(int n) {
        this.n = n;
    }

    public void assertSanity() {

        if (n != n) {
            throw new AssertionError("This statement is false.");
        }
    }
}