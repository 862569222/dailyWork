package cn.daily.juc.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2020/9/1 22:41
 */
public class TestConcurrentLinkedQueue {
    static Queue queue = new ConcurrentLinkedQueue();

    static {
        for (int i = 0; i <1000 ; i++) {
            queue.add("编号："+i);
        }
    }

    public static void main(String[] args) {

        List<Thread> list = new ArrayList();
        for (int i = 0; i <10 ; i++) {
            list.add(new Thread(()->{
               while (true){
                   Object poll = queue.poll();
                   if(poll == null){
                       break;
                   }
                   System.out.println("取出："+poll);
               }
            }));
        }

        list.forEach(i->{i.start();});

    }
}
