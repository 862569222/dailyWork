package cn.daily.juc.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: 实现一个容器，提供两个方法，add，size
 *  * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2 给出提示并结束
 * @date 2020/8/26 7:11
 */
public class Test3 {
    volatile   List list = new ArrayList();
    static CountDownLatch t1Latch = new CountDownLatch(1);
    static CountDownLatch t2Latch = new CountDownLatch(1);
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

            for (int i = 0; i <10 ; i++) {
                System.out.println(i);
                test1.add(i);
                if (test1.size() == 5) {
                    t2Latch.countDown();
                    try {
                        t1Latch.await();
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

                if(test1.size()!=5) {

                    System.out.println("t2线程阻塞");
                    try {
                        t2Latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    t1Latch.countDown();

                }else {
                    System.out.println("t2线程结束");
                    t1Latch.countDown();
                    break;

                }


            }

        },"t2").start();
    }

}
