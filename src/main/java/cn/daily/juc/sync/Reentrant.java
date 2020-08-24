package cn.daily.juc.sync;

import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;

/**
 * 一个同步方法调用另一个同步方法，一个线程已经拥有某个对象的锁，再次申请的时候仍然会获得到该对象的锁，
 * 也就是说synchronized 获得的锁是可重入的
 */

public class Reentrant  {
    static synchronized void  m (){

        System.out.println("method m start :"+Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m2();
    }

    static synchronized void  m2 (){

        System.out.println("method m2 start :"+Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        m();
    }
}
