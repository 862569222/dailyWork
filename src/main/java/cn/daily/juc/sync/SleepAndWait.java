package cn.daily.juc.sync;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: sleep方法必须要指定时间，wait方法可以不指定时间
 *              sleep释放cpu执行权，但不会释放锁，wait会释放掉cpu执行权并且释放掉锁
 * @date 2021/3/26 7:48
 */
public class SleepAndWait implements Runnable {
    static  Object object = new Object();
    public  void  m1() throws InterruptedException {
        synchronized (object){
            Thread.sleep(3000);
            System.out.println("m1执行。。");
        }
        System.out.println("m1释放锁");
    }

    public void  m2() throws InterruptedException {
        synchronized (object){
            System.out.println("m2执行");
            object.wait();
            System.out.println("m2执行完毕");
        }

    }

    public void  m3() throws InterruptedException {
        synchronized (object){
            System.out.println("m3执行");
            object.wait();
            System.out.println("m3执行完毕");
        }

    }
    public static void main(String[] args) throws InterruptedException {
        SleepAndWait sleepAndWait = new SleepAndWait();
        sleepAndWait.m1();
        //sleepAndWait.m2();
        Thread t1 = new Thread(new SleepAndWait(),"t1");
        t1.start();
        Thread t2 = new Thread(() -> {
            try {
                new SleepAndWait().m3();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2");
        t2.start();

        Thread.sleep(1000);
        Thread t3 = new Thread(new Notify(),"t3");
        t3.start();

        System.out.println("主线程执行完毕");
    }

    @Override
    public void run() {
        try {
            m2();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    static class Notify implements Runnable{

        @Override
        public void run() {
            synchronized (object){
                //object.notify();
                object.notifyAll();
                System.out.println("notify 唤醒其他等待线程");
            }
        }
    }
}
