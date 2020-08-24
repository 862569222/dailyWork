package cn.daily.juc.cas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * complier and swap
 *
 * 主要实现在unsafe 类中实现  unsafe可以直接操作内存 修改内存中的内容
 */
public class C1 {
    AtomicInteger atomicInteger = new AtomicInteger(0);

    void  m(){
        for (int i=0;i<10000 ;i++){
            atomicInteger.incrementAndGet();//相当于 count++
        }
    }

    public static void main(String[] args) {
        C1 C1 = new C1();
        List<Thread> threadList = new ArrayList<>();
        for (int i= 0;i<10 ;i++){
            threadList.add(new Thread(C1::m));
        }

        threadList.forEach((o)->o.start());
        threadList.forEach((o)-> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(C1.atomicInteger);
    }
}
