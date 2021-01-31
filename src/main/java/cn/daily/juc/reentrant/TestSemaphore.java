package cn.daily.juc.reentrant;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description:   限流
 * @date 2020/8/25 7:33
 */
public class TestSemaphore {
    static Semaphore semaphore = new Semaphore(1);//参数控制线程数

    public static void main(String[] args) {
        new Thread(()->{
            try {
                semaphore.acquire();
                System.out.println("T1 RUNNING..........");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("T1 RUNNING..........");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
            }
        }).start();

        new Thread(()->{
            try {
                semaphore.acquire();
                System.out.println("T2 RUNNING..........");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("T2 RUNNING..........");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
            }
        }).start();
    }
}
