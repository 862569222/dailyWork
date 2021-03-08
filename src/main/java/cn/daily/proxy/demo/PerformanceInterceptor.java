package cn.daily.proxy.demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/2/20 15:40
 */
public class PerformanceInterceptor implements InvocationHandler {
    private Object proxied;

    public PerformanceInterceptor(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object obj = method.invoke(proxied, args);
        long endTime = System.currentTimeMillis();
        System.out.println("Method " + method.getName() + " execution time: " + (endTime - startTime) * 1.0 / 1000 + "s");
        return obj;
    }
}
