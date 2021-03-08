package cn.daily.string;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

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
}
