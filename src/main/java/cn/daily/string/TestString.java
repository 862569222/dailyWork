package cn.daily.string;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 */
public class TestString {

 /*   public static void main(String[] args) {
        String str = "qqqqqqqqqqqqqqqqq";
        String str2 = "bbbbbbbbbbbbbb";
        String wwwwwwwwwww = str +str2;
        System.out.println(wwwwwwwwwww);


    }*/
    public static String balance(String[] str){
        String ret="";
        for (int i = 0; i <str.length ; i++) {
            ret+=str[i];
        }
        return ret;
    }
    public static String change(String[] str){
        StringBuilder ret = new StringBuilder();

        for (int i = 0; i <str.length ; i++) {
            ret.append("a");
        }
        return ret.toString();
    }

    public static void main(String[] args) {
        String[]  strs={"hello","world","What"};
        //balance(strs);
        char a = 97;
        System.out.println(a);
        //change(strs);
    }
    @Test
    public void strings(){
        String a ="zhaibo";//这个引用直接指向的是常量池中的地址
        String b = new String("zhaibo");//这种方式是在堆中创建一个对象 对象中存储了指向这个字符串在常量池中的地址
        String c = new String(a).intern();
        System.out.println(c);
        System.out.println(a==c);
        System.out.println(a==b);
    }

}
