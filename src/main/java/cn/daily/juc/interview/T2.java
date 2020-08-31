package cn.daily.juc.interview;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: 写一个固定容量的同步容器，拥有put和get方法，以及getcount方法，能够支持2个生产者线程
 * 以及10个消费者线程的阻塞调用
 * @date 2020/8/26 21:30
 */
public class T2<T> {
    LinkedList<T> list = new LinkedList<T>();
    static int MAX = 10;
    static int count = 0;
    private  Lock lock  = new ReentrantLock();
    Condition consumer = lock.newCondition();
    Condition product = lock.newCondition();
     void put(T t){
         try {
             lock.lock();
             while (list.size() == 10){
                 try {
                     consumer.await();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
             list.add(t);
             count++;
             product.signalAll();
         }finally {
             lock.unlock();
         }


    }
     T get(){
         T remove;
         try {
             lock.lock();
             while (list.size()==0){
                 try {
                     product.await();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
             remove = list.removeFirst();
             count--;
             consumer.signalAll();
         }finally {
             lock.unlock();
         }


        return remove;
    }

    public static void main(String[] args) {

        T2 t1 = new T2();
        for (int i = 0; i <10 ; i++) {
            new Thread(()->{
                for (int j = 0; j <5 ; j++) {
                    System.out.println(t1.get());
                }
            },"c"+i).start();
        }


        for (int i = 0; i <2 ; i++) {
            new Thread(()->{
                for (int j = 0; j <25 ; j++) {
                    t1.put(Thread.currentThread().getName()+" "+j);

                }
            },"p"+i).start();

        }

    }
}
