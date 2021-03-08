package cn.daily.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/2/20 14:06
 */
public class ProxyFactory implements MethodInterceptor {

    //被代理对象
    private Object target;

    public ProxyFactory(Object target){
        this.target = target;
    }
    //为被代理对象创建一个代理对象
    public Object getProxyInstance(){
        //这个是工具类
        Enhancer en = new Enhancer();
        //将被代理类设置为父类
        en.setSuperclass(target.getClass());
        //设置回调
        en.setCallback(this);
        //创建子类（代理对象）
        return en.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("前置执行....");
        //执行被代理类的方法
        Object returnValue = method.invoke(target,objects);
        System.out.println("后置执行....");
        return returnValue;
    }
}
