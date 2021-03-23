package cn.daily.juc.cas;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/3/24 6:56
 */
public class AtomicReferenceTest {
    AtomicReference<Student> atomicReference = new AtomicReference();

    @Test
    public void get(){
        Student student = new Student(1L, "aaa", "aaa");
        Student student1 = new Student(2L, "bbb", "bbb");
        atomicReference.set(student);
        Student x = atomicReference.get();
        boolean b = atomicReference.compareAndSet(student,student1 );
        if(b){
            atomicReference.set(student1);
            System.out.println(atomicReference.get());
        }
        System.out.println(x);
        atomicReference.set(student1);
    }
}
