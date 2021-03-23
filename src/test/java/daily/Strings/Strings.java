package daily.Strings;


import org.junit.jupiter.api.Test;

public class Strings {


    @Test
    public void strings(){
        String a ="zhaibo";//这个引用直接指向的是常量池中的地址
        String b = new String("zhaibo");//这种方式是在堆中创建一个对象 对象中存储了指向这个字符串在常量池中的地址
        String c = new String(a).intern();
        System.out.println(c);
        System.out.println(a==c);
        System.out.println(a==b);
    }

    @Test
    public void length(){
        int[][] a = new int[2][];
        int[][] b = new int[5][2];
        System.out.println(a.getClass());
        System.out.println(b.length);
    }
}
