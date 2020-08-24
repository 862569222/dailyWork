package cn.daily.juc.sync;

public class Join {
    public static void main(String[] args) {
        testjoin();
    }

    static void testjoin(){
        Thread ti = new Thread(()->{
            for (int i=0;i<100;i++){
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        ti.start();
        Thread t2 = new Thread(()->{
            try {
                System.out.println("等待线程 ti执行");
                ti.join();
                System.out.println("等待线程 ti执行完毕 t2 执行");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();
    }
}
