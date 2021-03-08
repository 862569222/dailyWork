package cn.daily.proxy;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/2/20 12:58
 */
public class Man implements Person {
    @Override
    public void eat() {
        System.out.println("吃饭。。。");
    }
}
