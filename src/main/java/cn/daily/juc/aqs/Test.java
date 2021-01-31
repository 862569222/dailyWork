package cn.daily.juc.aqs;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2020/9/15 22:47
 */
public class Test {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        SynchronousQueue<Object> objects = new SynchronousQueue<>();

        new Thread(()->{
            lock.lock();
            lock.unlock();
        }).start();

    }
}
