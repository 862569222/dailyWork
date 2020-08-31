package cn.daily.juc.interview;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: 写一个固定容量的同步容器，拥有put和get方法，以及getcount方法，能够支持2个生产者线程
 * 以及10个消费者线程的阻塞调用
 * @date 2020/8/26 21:30
 */
public class T1<T> {
    List<T> list = new LinkedList<T>();
    static int MAX = 10;
    static int count = 0;
    synchronized void put(T t){
        while (list.size() == 10){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        list.add(t);
        count++;
        this.notifyAll();

    }
    synchronized T get(){
        while (list.size()==0){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        T remove = list.remove(0);
        count--;
        this.notifyAll();

        return remove;
    }

    public static void main(String[] args) {

        T1 t1 = new T1();
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
