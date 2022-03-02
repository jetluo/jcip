package com.jetluo.jcip.chapter07;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @ClassName ReaderThread
 * @Description 通过改写interrupt方法将非标准的取消操作封装在Thread中
 * @Author jet
 * @Date 2022/1/21 14:51
 * @Version 1.0
 **/
public class ReaderThread extends Thread{
    private  static  final  int BUFSZ = 512;
    private  final Socket socket;
    private final InputStream in;

    public ReaderThread(Socket socket)throws IOException{
        this.socket = socket;
        this.in = socket.getInputStream();
    }
    @Override
    public void run() {
        try {
            byte[] buf = new byte[BUFSZ];
            while (true) {
                int count = in.read(buf);
                if (count < 0){
                    break;
                }else  if(count > 0){
                    processBuff(buf, count);
                }
            }
        }catch (IOException e){

        }
    }

    @Override
    public void interrupt() {
        try {
            socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }finally {
            super.interrupt();
        }
    }
    public void processBuff(byte[] buf,int count){}
}
