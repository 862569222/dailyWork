package cn.daily.proxy.demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/2/20 15:57
 */
public class LogInterceptorTest {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        Class<?> cls = userDao.getClass();
        InvocationHandler handler = new LogInterceptor(userDao);
        UserDao proxy = (UserDao) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), handler);
        proxy.addUser(new User("tom"));
        proxy.deleteUser("tom");
        proxy.updateUser("tom");
    }
}
