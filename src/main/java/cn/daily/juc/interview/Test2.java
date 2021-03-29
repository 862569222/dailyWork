package cn.daily.juc.interview;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: 实现一个容器，提供两个方法，add，size
 *  * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2 给出提示并结束
 * @date 2020/8/26 7:11
 */
public class Test2 {
    volatile   List list = new ArrayList();
    /*synchronized*/  void add(Object object){
        list.add(object);
    }

    /*synchronized*/  int  size(){
        return list.size();
    }


    public static void main(String[] args) {
        Test1 test1 = new Test1();
        Object o = new Object();
        new Thread(()->{
            System.out.println("t1线程开始");
            /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            for (int i = 0; i <10 ; i++)
                synchronized (o) {
                    System.out.println(i);
                    test1.add(i);
                    if (test1.size() == 5) {

                        o.notify();//唤醒阻塞线程但是并不释放锁
                        try {
                            o.wait();//释放锁 当前线程进入阻塞状态
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                }
            System.out.println("t1线程结束");
        },"t1").start();

        new Thread(()->{
            System.out.println("t2线程开始");
            while (true){

                synchronized (o){
                    System.out.println("t2线程获取锁");
                    if(test1.size()!=5) {
                        try {
                            System.out.println("t2线程阻塞");
                            o.wait();
                            System.out.println("t2线程阻塞");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        System.out.println("t2线程结束");
                        o.notify();
                        break;

                    }

                }

            }

        },"t2").start();
    }

}
