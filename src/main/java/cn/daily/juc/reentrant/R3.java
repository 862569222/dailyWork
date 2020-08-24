package cn.daily.juc.reentrant;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2020/8/24 7:28
 */
public class R3 {
    static  int count =0;
    public static void main(String[] args) {

        Thread[] threads = new Thread[100];
        Lock lock = new ReentrantLock(true);
        CountDownLatch latch = new CountDownLatch(threads.length);


        for (int i=0;i<threads.length;i++){
            threads[i] = new Thread(()->{

                for (int j=0;j<10000;j++){
                    try {
                        lock.lock();
                        count++;
                      }finally {
                        lock.unlock();
                    }
                }
                System.out.println("线程 countDown："+Thread.currentThread().getName());
                latch.countDown();

            });
        }

        for (int i=0;i<threads.length;i++){
            threads[i].start();
        }
        System.out.println("随机输出线程未必都结束："+count);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("线程执行完毕："+count);

    }

}
