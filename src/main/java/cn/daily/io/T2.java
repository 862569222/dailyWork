package cn.daily.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2021/6/22 11:47
 */
public class T2 {
    public static void main(String[] args) throws IOException {
        FileInputStream fi = new FileInputStream("D:\\log.txt");
        FileOutputStream fo = new FileOutputStream("D:\\2.txt");
        byte[] a = new byte[1024];
        int len = 0;
        int i= 0;
        System.out.println("-------------start---------------");
        while((len = fi.read(a)) != -1){
            fo.write(a,0,len );
            System.out.println("写入文件"+(i++));
        }
        fo.close();
        fi.close();
        System.out.println("-------------end---------------");
    }
}
