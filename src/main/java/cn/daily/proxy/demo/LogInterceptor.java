package cn.daily.proxy.demo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/2/20 15:55
 */
public class LogInterceptor implements InvocationHandler {
    private Object proxied;

    public static final String path = "run.log";

    public LogInterceptor(Object proxied) {
        this.proxied = proxied;
    }

    public String beforeMethod(Method method) {
        return getFormatedTime() + " Method:" + method.getName() + " start running\r\n";
    }

    public String afterMethod(Method method) {
        return getFormatedTime() + " Method:" + method.getName() + " end running\r\n";
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        write(path, beforeMethod(method));
        Object object = method.invoke(proxied, args);
        write(path, afterMethod(method));
        return object;
    }

    public String getFormatedTime() {
        DateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return formater.format(System.currentTimeMillis());
    }

    public void write(String path, String content) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(path), true);
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
