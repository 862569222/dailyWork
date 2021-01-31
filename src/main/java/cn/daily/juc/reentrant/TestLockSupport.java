package cn.daily.juc.reentrant;

import java.util.concurrent.locks.LockSupport;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2020/8/25 23:08
 */
public class TestLockSupport {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            for (int i = 0; i <10 ; i++) {
                System.out.println(i);
                if(i==5){
                    LockSupport.park();
                    //LockSupport.park();
                }
            }
        });
        thread.start();
        LockSupport.unpark(thread);


    }
}
