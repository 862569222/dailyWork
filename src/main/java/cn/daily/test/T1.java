package cn.daily.test;

import cn.daily.juc.container.Student;
import lombok.SneakyThrows;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2021/7/5 14:46
 */

public class T1 {
    @Test
    public void t1(){
        Integer a = 128;
        Integer b = 128;
        System.out.println(a==b);
    }

    @Test
    public void t2(){
        Class<Student> studentClass = Student.class;
        Method[] declaredMethods = studentClass.getDeclaredMethods();
        Arrays.stream(declaredMethods).forEach(o->{
            System.out.println(o);
        });

    }

    @SneakyThrows
    @Test
    public void t3()  {
        while (true){
            Thread.sleep(1000);
            System.out.println("...");
        }
    }
}
