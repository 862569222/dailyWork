package cn.daily.juc.container;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2021/7/1 16:02
 */
public class TestList {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();
        Student student1 = new Student();
        student1.setId("1");
        student1.setName("zb");
        Student student2 = new Student();
        student2.setId("2");
        student2.setName("wy");
        System.out.println(student1==student2);
       /* List<String> collect = list.stream().map(Student::getId).map(it->{})collect(Collectors.toList());
        Map<String, Student> collect1 = list.stream().collect(Collectors.toMap(Student::getId, Function.identity(), (o1, o2) -> o1));
        Map<String, List<Student>> collect2 = list.stream().collect(Collectors.groupingBy(Student::getDept));*/
    }

    @Test
    public void t1(){
        System.out.println("");
        Long aa = new Long(1L);
        Long a = 1L;
        System.out.println(a.equals(aa));

        System.out.println("");
        System.out.println("");
    }


    @Test
    public void t2(){

        String a = "ababbabbabbbabaaababababba";
        char[] chars = a.toCharArray();
        boolean flagA = false;
        boolean flagB = false;
        int count = 0;
        for (int i = 0; i <chars.length ; i++) {
            if(chars[i] == 'a'){
                flagA = true;
            }
            if(chars[i] == 'b'){
                flagB = true;
            }
            if(flagA && flagB){
                count++;
                flagA = false;
                flagB = false;
            }
        }
        System.out.println(count);
    }
}
