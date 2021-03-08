package cn.daily.proxy;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/2/20 12:59
 */
public class ManProxy implements Person {
    private  Person target;

    public ManProxy(Person target) {
        this.target = target;
    }
    @Override
    public void eat() {
        System.out.println("吃肉。。");
        target.eat();
        System.out.println("吃青菜。。。");
    }
}
