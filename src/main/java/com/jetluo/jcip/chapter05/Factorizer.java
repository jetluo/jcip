package com.jetluo.jcip.chapter05;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

/**
 * @ClassName Factorizer
 * @Description TODO
 * @Author jet
 * @Date 2022/1/11 16:29
 * @Version 1.0
 **/
public class Factorizer extends GenericServlet implements Servlet {
    private final Computable<BigInteger, BigInteger[]> c = new Computable<BigInteger, BigInteger[]>() {
        @Override
        public BigInteger[] compute(BigInteger arg) throws InterruptedException {
            return factor(arg);
        }
    };
    private final Computable<BigInteger, BigInteger[]> cache = new Memoizer<BigInteger, BigInteger[]>(c);


    BigInteger[] factor(BigInteger i) {
        return new BigInteger[]{i};
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger i = extractFromRequest(servletRequest);
        try {
            encodeIntoResponse(servletResponse,cache.compute(i));
        } catch (InterruptedException e) {
           encodeError(servletResponse,"factorzation InterruptedException");
        }
    }
    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    void encodeError(ServletResponse resp, String errorString) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

}
