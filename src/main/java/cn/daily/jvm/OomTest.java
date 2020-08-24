package cn.daily.jvm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OomTest {
    public static void main(String[] args) {
        List list = new ArrayList();
        int i=0;
        int j=0;

        while (true){
            list.add(new User(i++, UUID.randomUUID().toString()));
            new User(j--, UUID.randomUUID().toString());
        }
    }
}
