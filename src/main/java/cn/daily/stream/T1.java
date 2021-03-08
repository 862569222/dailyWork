package cn.daily.stream;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/3/7 21:08
 */

public class T1 {


    @Test
    public void t1(){
        List<Integer> sourceList =  new ArrayList<>();
        sourceList.add(1);
        sourceList.add(2);
        sourceList.add(3);
        sourceList.add(4);

        List<Integer> targetList =  new ArrayList<>();
        targetList.add(3);
        targetList.add(5);
        targetList.add(6);
        targetList.add(7);

        //求与目标List的交集
        List<Integer> list = sourceList.stream().filter(source -> targetList.contains(source)).collect(Collectors.toList());
        System.out.println("交集："+list.toString());
        //求与目标List的差集
        List<Integer> list1 = sourceList.stream().filter(source -> !targetList.contains(source)).collect(Collectors.toList());
        System.out.println("差集："+list1.toString());
        //求与原list的差集
        List<Integer> list2 = targetList.stream().filter(target -> !sourceList.contains(target)).collect(Collectors.toList());
        System.out.println("差集："+list2.toString());
    }

    @Test
    public void t2(){
        List<Person> personList =new ArrayList<>();
        Person person = new Person(1,"小明","男",2);
        Person person2 = new Person(1,"小红","女",2);
        Person person3 = new Person(2,"小军","男",3);
        Person person4 = new Person(3,"小军","男",4);
        Person person5 = new Person(4,"小军","男",6);
        personList.add(person);
        personList.add(person2);
        personList.add(person3);
        personList.add(person4);
        personList.add(person5);
        //去除重复id的数据
        List<Person> personList1  = removeRepeatId(personList);
        personList1.forEach(person1 -> System.out.println(person1.toString()));
        /**
         * 找出list中的对象,对应的参数出现了几次
         */
        Map<Integer, Long> idRecount  = personList.stream().collect(Collectors.groupingBy(Person::getId, Collectors.counting()));
        idRecount.forEach((k,v) -> System.out.println(k+"出现"+v+"次。"));
        //找出List中重复的对象
        List<Integer> reIds = new ArrayList<>();
        idRecount.forEach((k,v)-> {if(v>1) reIds.add(k);});
        List<Person> collect = personList.stream().filter(person1 -> reIds.contains(person1.getId())).collect(Collectors.collectingAndThen(
                Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Person::getId))),ArrayList:: new
        ));

        collect.forEach(person1 -> System.out.println(person1.toString()));


        //list转map
        Map<Integer, Person> collect2 = personList.stream().collect(Collectors.toMap(Person::getId,Person ->{
            return Person;
        },(Person p1 ,Person p2)->{
            return p2;
        }));
        Map<Integer, List<Person>> collect1 = personList.stream().collect(Collectors.toMap(Person::getId, Person -> {
            List<Person> personList2 = new ArrayList<>();
            personList2.add(Person);
            return personList2;
        },(List<Person> value1, List<Person> value2) -> {
            value1.addAll(value2);
            return value1;
        }));
        System.out.println(collect1);
    }

    private List<Person> removeRepeatId(List<Person> personList) {
        TreeSet<Person> people = new TreeSet<>(Comparator.comparing(Person::getId).thenComparing(Person::getAge));
        people.addAll(personList);
        return new ArrayList<>(people);
    }

}
