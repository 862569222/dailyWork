package cn.daily.juc.interview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description:实现一个容器，提供两个方法，add，size
 * 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2 给出提示并结束
 * @date 2020/8/25 23:13
 */
public class Test1 {
    //volatile   List list = new ArrayList();
    volatile   List list = Collections.synchronizedList(new ArrayList<>());
      /*synchronized*/  void add(Object object){
        list.add(object);
    }

      /*synchronized*/  int  size(){
        return list.size();
    }


    public static void main(String[] args) {
        Test1 test1 = new Test1();
        new Thread(()->{
            for (int i = 0; i <10 ; i++) {
                System.out.println(i);
                test1.add(i);
                /*try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        }).start();

        new Thread(()->{

            while (true){

                if(test1.size()==5) {
                    break;
                }
            }
        }).start();
    }
}
