package cn.daily.juc.container;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2020/9/1 21:02
 */
public class TestSynchronsizedHashMap {
    static Map<UUID,UUID> hashtable = Collections.synchronizedMap(new HashMap<UUID,UUID>());
    static UUID[] keys = new UUID[1000000];
    static UUID[] values = new UUID[1000000];

    static {
        for (int i = 0; i <1000000 ; i++) {
            keys[i] = UUID.randomUUID();
            values[i] = UUID.randomUUID();
        }
    }

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i <100 ; i++) {
            int finalI = i;
            threads.add(new Thread(()->{
                for (int j = (finalI *10000); j <(finalI+1)*10000 ; j++) {
                    hashtable.put(keys[j],values[j]);
                }
            })) ;
        }

        threads.forEach(i->{i.start();});
        threads.forEach(i->{
            try {
                i.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();

        System.out.println(end-start);
        System.out.println(hashtable.size());


    }


}
