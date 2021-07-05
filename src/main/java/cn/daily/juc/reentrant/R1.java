package cn.daily.juc.reentrant;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhaibo
 * @title: R1
 * @projectName dailyWork
 * @description:  lock  必须手动释放锁
 * @date 2020/8/2321:40
 */
public class R1 {
    static Lock lock = new ReentrantLock();
    static  Condition c = lock.newCondition();
    int count =0;
    void m () throws InterruptedException {
        for (int i=0;i<10;i++){

            try {
                lock.lock();
                System.out.println(count++);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                //唤醒其他等待线程需要在锁中调用
                c.signalAll();
                lock.unlock();
                //释放锁后调此方法会报错
                //c.signalAll();
                TimeUnit.SECONDS.sleep(1);
            }
            /*if(i==2){
                m2();
            }*/
        }

    }

    void m2() throws InterruptedException {
        while (true){
            try {
                List list = new ArrayList();
                lock.lock();
                System.out.println("m2 start...........");
                c.await();
            }finally {
                System.out.println("m2 unlock...........");
                lock.unlock();
                TimeUnit.SECONDS.sleep(1);
            }
        }


    }

    public static void main(String[] args) {
        R1 r1 = new R1();
        new Thread(() -> {
            try {
                r1.m();
            } catch (InterruptedException e) {
                //c.signalAll();
                e.printStackTrace();
            }
        },"t1").start();
        /*try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        new Thread(() -> {
            try {
                r1.m2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2").start();

    }
}
