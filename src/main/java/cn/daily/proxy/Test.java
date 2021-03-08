package cn.daily.proxy;

import java.lang.reflect.Proxy;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/2/20 13:02
 */
public class Test {
    public static void main(String[] args) {

        //被代理对象
        Person target = new Man();
        System.out.println("被代理对象打印："+target.getClass());
        //创建代理对象
        Person proxy = (Person) new ProxyFactory(target).getProxyInstance();

        System.out.println("生成的代理对象打印："+proxy.getClass());

        proxy.eat();
        /*Person man = new Man();

        Person manProxy = new ManProxy(man);
        manProxy.eat();*/
    }
}
