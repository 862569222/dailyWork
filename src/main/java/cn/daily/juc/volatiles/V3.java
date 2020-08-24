package cn.daily.juc.volatiles;

import java.util.ArrayList;
import java.util.List;

/**
 * volatile不能保证多个线程共同修改数据带来的数据不一致问题，volatile不能代替synchronized
 */
public class V3 {
    private volatile int count = 0;
    
    public /*synchronized*/ void m(){
        for (int i=0;i<10000;i++) count++;
    }

    public static void main(String[] args) {
        V3 v3 = new V3();
        List<Thread> threadList = new ArrayList<>();
        for (int i=0;i<100;i++){
            threadList.add(new Thread(v3::m));
        }

        threadList.forEach((o)->o.start());

        threadList.forEach((o)-> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(v3.count);
    }
    
}
