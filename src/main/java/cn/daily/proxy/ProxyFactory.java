package cn.daily.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/2/20 13:45
 */
public class ProxyFactory {
    //被代理对象
    private Object target;
    public ProxyFactory(Object target){
        this.target = target;
    }

    //为被代理对象生成代理对象
    public Object getProxyInstance(){
        return Proxy.newProxyInstance(
                //指定当前被代理对象使用的类加载器
                target.getClass().getClassLoader(),
                //指定被代理对象实现的接口类型，这里是一个数组，代理可能实现多个接口
                target.getClass().getInterfaces(),
                //事件处理，当我们在执行被代理对象的方法的时候，会触发这个处理器，会把当前执行的被代理对象的方法作为参数传入进去
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("执行前置方法");
                        //执行目标方法
                        Object returnValue = method.invoke(target, args);
                        System.out.println("执行后置方法");
                        return returnValue;
                    }
                }
        );
    }
}

