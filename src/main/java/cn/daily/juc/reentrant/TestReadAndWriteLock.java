package cn.daily.juc.reentrant;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: 读锁可以被其他读线程共享，写锁独占，并且只有在写线程写锁释放后 读线程才能获取读锁去读数据
 * @date 2020/8/25 6:54
 */
public  class TestReadAndWriteLock {
    static Lock lock = new ReentrantLock();

    static void read(Lock lock){
        try {
            lock.lock();
            System.out.println("read over;");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    static void write(Lock lock){
        try {
            lock.lock();
            System.out.println("write over;");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }



    public static void main(String[] args) {
        ReentrantReadWriteLock reentrantReadWriteLock =  new ReentrantReadWriteLock();
        Lock readLock = reentrantReadWriteLock.readLock();
        Lock writeLock = reentrantReadWriteLock.writeLock();

        //Runnable readR = () -> read(lock);
       // Runnable writeR = () -> write(lock);
        Runnable writeR = () -> write(writeLock);
        Runnable readR = () -> read(readLock);
        for (int i=0;i<1800;i++) new Thread(readR).start();
        for (int i=0;i<20;i++) new Thread(writeR).start();
        for (int i=0;i<18;i++) new Thread(readR).start();
    }
}
