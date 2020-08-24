package cn.daily.juc.volatiles;

public class V1 {
    private /*volatile*/ boolean running = true;

    public void  m (){
        System.out.println("m start....");
        while (running){

        }
        System.out.println("m end......");

    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public static void main(String[] args) {
        V1 vo = new V1();
        new Thread(vo::m,"T1").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        vo.setRunning(false);

    }
}
