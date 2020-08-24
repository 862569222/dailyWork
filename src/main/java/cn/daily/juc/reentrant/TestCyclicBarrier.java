package cn.daily.juc.reentrant;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2020/8/24 23:15
 */
public class TestCyclicBarrier {
    static CyclicBarrier barrier = new CyclicBarrier(20,()->{
        System.out.println("线程满20 执行次逻辑");
    });

    public static void main(String[] args) {
        for (int i= 0;i<100;i++){
            new Thread(()->{
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}
