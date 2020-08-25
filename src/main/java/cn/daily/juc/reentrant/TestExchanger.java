package cn.daily.juc.reentrant;

import java.util.concurrent.Exchanger;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: 交换器 两两交换（游戏装备交易场景？）  一个线程调用exchange() 方法后阻塞等待另一个线程进入
 *   在另一个线程调用exchange()方法 两个线程交换数据后 继续向下执行
 * @date 2020/8/25 8:00
 */
public class TestExchanger {
    static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        new Thread(()->{
            String s = "t1";
            try {
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" "+ s);
        },"T1").start();

        new Thread(()->{
            String s = "t2";
            try {
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" "+ s);
        },"T2").start();
    }
}
