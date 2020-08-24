package cn.daily.jvm;

public class Test {
    public static void main(String[] args) {
        int math = math();
        System.out.println(math);
    }

    public static int  math(){
        int a = 1;
        int b = 2;
        int c = (a+b)*10;
        return c;
    }
}
