package lang;

import org.junit.Test;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/3/17 7:08
 */
public final class String {
    public static void main(String[] args) {
        System.out.println("aaa");
    }
    public Object getStr (){
        return "qweq";
    }

    @Test
    public void t1(){
        b b = new b();
        b.m1();
        int i = m1();
        System.out.println(i);
    }

    private int m1() {
        try {
            return 1;
        }catch (Exception e){
            return 2;
        }finally {
            return 3;
        }
    }



}

class a {
    /*public  void m1(){
        System.out.println(1);
    }*/
}

class b extends a implements c{
    /*public  void m1(){
        System.out.println(2);
    }*/
}
interface c {
    default void m1(){
        System.out.println(3);
    }
}
