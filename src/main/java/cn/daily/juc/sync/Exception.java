package cn.daily.juc.sync;

/**
 * 程序在执行过程中，如果出现异常，默认情况下锁是会被释放的
 * 所以在并发处理的过程中，有异常要多加小心，不然可能会出现不一致的情况
 * 比如，一个webapp处理过程中，多个servlet线程共同访问同一个资源，这时如果异常处理不合适，在第一个线程出现
 * 异常时，其他线程会进入同步代码区，可能会访问到异常产生的数据
 * 因此要小心的处理同步业务中的异常
 */
public class Exception   {
     int count = 10;
     synchronized void m() {
        while (true){
            count --;
            System.out.println("thread:"+Thread.currentThread().getName()+" ===》count："+count);
            try {
                Thread.sleep(1000);
                if (count==5){
                    int i =1/0;//此处抛出异常，锁将会被释放，可以catch 让循环继续

                }
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        Exception  e = new Exception();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                e.m();
            }
        };

        new Thread(r,"ti").start();
        new Thread(r,"t2").start();

    }
}
