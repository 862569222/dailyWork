package cn.daily.proxy.cglib;

import cn.daily.proxy.Man;
import cn.daily.proxy.Person;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/2/20 13:02
 */
public class Test {
    public static void main(String[] args) {

        Man man = new Man();

        Man manProxy = (Man) new ProxyFactory(man).getProxyInstance();
        manProxy.eat();
    }
}
