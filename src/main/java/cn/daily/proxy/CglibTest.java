package cn.daily.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2021/7/9 16:38
 */
public class CglibTest {

    static class TestClass {
        public void t1(){
            System.out.println("被代理类。。。");
        }
    }

    static class MyInterceptor implements MethodInterceptor{

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            Object o1 = methodProxy.invokeSuper(o, objects);
            System.out.println("代理类执行。。。。。");
            return null;
        }
    }

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        //设置父类，Cglib基于父类生成代理子类
        enhancer.setSuperclass(TestClass.class);
        //设置回调
        enhancer.setCallback(new MyInterceptor());

        //创建代理类
        TestClass o = (TestClass)enhancer.create();

        o.t1();


    }

}
