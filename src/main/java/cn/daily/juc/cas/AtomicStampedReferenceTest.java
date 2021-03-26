package cn.daily.juc.cas;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/3/24 7:40
 */
public class AtomicStampedReferenceTest {
    Student student = new Student(1L, "aaa", "aaa");
    AtomicStampedReference<Student> atomicStampedReference =
            new AtomicStampedReference(student,1);

    @Test
    public void get (){

        Student student1 = new Student(2L, "bbb", "bbb");
        //atomicStampedReference.set(student,1);
        if(atomicStampedReference.compareAndSet(student,student1,atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1)){
            System.out.println(atomicStampedReference.getReference().toString()+ "\n"+atomicStampedReference.getStamp());
        }
    }

}
