package cn.daily.juc.reentrant;

import java.util.concurrent.Phaser;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2020/8/24 22:14
 */
public class TestPhaser {
    static MarryPhaser phaser = new MarryPhaser();

    public static void main(String[] args) {
        //设定栅栏满足数  只有线程数齐了才能执行下一步操作
        phaser.bulkRegister(7);

        for (int i= 0 ;i< 5 ;i++){
            new Thread(new Person("p"+i)).start();
        }
        new Thread(new Person("新郎")).start();
        new Thread(new Person("新娘")).start();
    }

    static class MarryPhaser extends Phaser{
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase){
                case 0:
                    System.out.println("所有人到齐了！"+registeredParties);
                    return false;
                case 1:
                    System.out.println("所有人吃完了！"+registeredParties);
                    return false;
                case 2:
                    System.out.println("所有人离开了！"+registeredParties);
                    return false;
                case 3:
                    System.out.println("婚礼结束！新郎新娘抱抱！"+registeredParties);
                    return true;
                default:
                    return true;
            }

        }
    }

    static class Person implements Runnable{
        String name ;
        Person(String name){
            this.name = name;
        }
        void arrive(){
            System.out.println(name+"到达现场");
            System.out.println();
            phaser.arriveAndAwaitAdvance();
        }

        void eat(){
            System.out.println(name+"吃完了");
            System.out.println();
            phaser.arriveAndAwaitAdvance();
        }

        void leave(){
            if(!"新娘".equals(name) && !"新郎".equals(name)){
                System.out.println(name+"离开");
                System.out.println();
                phaser.arriveAndAwaitAdvance();
            }else {
                //新郎新娘不离开的话执行下面的逻辑
                phaser.arriveAndAwaitAdvance();
            }


        }

        void hug(){
            if("新娘".equals(name) || "新郎".equals(name)){
                phaser.arriveAndAwaitAdvance();
            }else {
                phaser.arriveAndDeregister();
            }

        }

        @Override
        public void run() {
            arrive();
            eat();
            leave();
            hug();

        }


    }

}