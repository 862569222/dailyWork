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
 * @description:    tryLock
 * @date 2020/8/2321:40
 */
public class R2 {
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
        boolean b = false;
        try {
            System.out.println("尝试获取锁");
            b = lock.tryLock(5, TimeUnit.SECONDS);
            if(b){
                System.out.println("成功拿到锁");
                System.out.println("m2 start...........");
            }else{
                System.out.println("获取锁失败。。。。。。");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(b){
                lock.unlock();
            }

        }

    }

    public static void main(String[] args) {
        R2 r1 = new R2();
        new Thread(r1::m).start();
        try {
            TimeUnit.SECONDS.sleep(11);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(r1::m2).start();

    }
}
