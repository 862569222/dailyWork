package cn.daily.compare;

import cn.daily.juc.cas.Student;
import cn.daily.stream.Person;
import org.junit.Test;
import org.w3c.dom.DOMImplementationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2021/7/9 16:02
 */
public class Compare {
    /**
     * 比较器Comparator 自定义实现比较器
     */
    @Test
    public void t1(){
        Student[] students = new Student[]{new Student( 1l,"1","zhangsan"),
                                            new Student( 4l,"1","lisi"),
                                            new Student( 3l,"1","wangwu")};

        Arrays.sort(students,new compareA());
        Arrays.stream(students).forEach(o->{
            System.out.println(o.getId());
        });
    }

    @Test
    public void t2(){
        Person[] person = new Person[]{new Person( 1,"1","zhangsan"),
                new Person( 4,"1","lisi"),
                new Person( 3,"1","wangwu")};

        Arrays.sort(person);
        Arrays.stream(person).forEach(o->{
            System.out.println(o.getId());
        });
    }

}


class compareA implements Comparator<Student>{


    @Override
    public int compare(Student o1, Student o2) {
        return (int) (o1.getId() -o2.getId());
    }


}