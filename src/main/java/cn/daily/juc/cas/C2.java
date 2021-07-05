package cn.daily.juc.cas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * 三种分方式实现多线程之间的数值递增
 *
 * synchronized加锁
 * atomicInteger
 * longAdder
 */
public class C2 {
    static LongAdder count1 = new LongAdder();
    static int count2 = 0;

    AtomicInteger count3 = new AtomicInteger(0);
    void  m(){
        for (int i=0;i<10000 ;i++){
            count3.incrementAndGet();//相当于 cou
            // nt++
        }
    }

    void m2(){
        for (int i=0;i<10000 ;i++){
            synchronized (this) {
                count2++;
            }
        }
    }


    void m3(){
        for (int i=0;i<10000 ;i++){
            count1.increment();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        C2 c2 = new C2();
        List<Thread> threadList = new ArrayList<>();
        for (int i= 0;i<1000 ;i++){
            threadList.add(new Thread(c2::m));
        }

        long start = System.currentTimeMillis();

        threadList.forEach((o)->o.start());
        threadList.forEach((o)-> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        long end = System.currentTimeMillis();
        System.out.println("Automic:"+c2.count3+" , time:"+(end-start));

        //=====================================================================

        Thread[] threads = new Thread[1000];
        Object object = new Object();

        for (int i =0;i<threads.length;i++){
            threads[i] = new Thread(c2::m2);

        }
        long start2 = System.currentTimeMillis();
        for (Thread thread:threads) thread.start();
        for (Thread thread:threads) thread.join();
        long end2 = System.currentTimeMillis();

        System.out.println("Sync:"+count2+" , time:"+(end2-start2));

        /*threadList.forEach((o)->{
            for (int i=0;i<100000;i++){
                synchronized (C2.class) {
                    count2++;
                }
            }
        });*/

        //===========================================================


        for (int i =0;i<threads.length;i++){
            threads[i]=new Thread(c2::m3);

        }

        long start3 = System.currentTimeMillis();
        for (Thread thread:threads) thread.start();
        for (Thread thread:threads) thread.join();
        long end3 = System.currentTimeMillis();

        System.out.println(C2.count1.sum());
        System.out.println("longAdder:"+count1+" , time:"+(end3-start3));

    }
}
