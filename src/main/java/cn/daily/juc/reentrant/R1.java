package cn.daily.juc.reentrant;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.TimeUnit;
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
    Lock lock = new ReentrantLock();
    int count =0;
    void m (){
        for (int i=0;i<10;i++){

            try {
                lock.lock();
                System.out.println(count++);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            /*if(i==2){
                m2();
            }*/
        }

    }

    void m2(){
        try {
            List list = new ArrayList();
            lock.lock();
            System.out.println("m2 start...........");
        }finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        R1 r1 = new R1();
        new Thread(r1::m).start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(r1::m2).start();

    }
}
