package cn.daily.juc.sync;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/3/27 7:06
 */
public class Daemon {
    static class task implements Runnable{
        boolean flag = true;
        @Override
        public synchronized void run() {
            while(flag){
                try {
                    System.out.println(Thread.currentThread().getName()+"-等待");
                    //Thread.sleep(1000);
                    wait();
                    System.out.println(Thread.currentThread().getName()+"唤醒");
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName()+"中断异常。。。");
                    flag = false;
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new task(), "t1 ");
        t1.start();
        Thread t2 = new Thread(new task(),"t2");
        t2.setDaemon(true);
        t2.start();
        int i = 0;
        while (i<50){
            if(++i == 50){
                t1.interrupt();
                //t2.interrupt();
            }
            System.out.println(Thread.currentThread().getName()+" - "+ i);
        }

        Thread.sleep(5000);
        System.out.println("主线程执行完毕。。");
    }



}
