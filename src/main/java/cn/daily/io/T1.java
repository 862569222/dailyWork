package cn.daily.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2021/6/22 10:51
 */
public class T1 {
    public static void main(String[] args) throws IOException {
        FileReader fr = new FileReader("D:\\log.txt");
        FileWriter fw = new FileWriter("D:\\1.txt");

        char[] a = new char[1024];
        int len = 0;
        int i= 0;
        System.out.println("-------------start---------------");
        while((len = fr.read(a)) != -1){
            fw.write(a,0,len );
            System.out.println("写入文件"+(i++));
        }
        fw.close();
        fr.close();
        System.out.println("-------------end---------------");

    }
}
