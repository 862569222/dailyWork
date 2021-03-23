package daily;

import cn.daily.spring.config.Config;
import cn.daily.spring.config.Person;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description:
 * @date 2020/12/27 21:38
 */
public class Spring {
    @Test
    public void test(){
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(Config.class);
        Person person = (Person) app.getBean("person");
        System.out.println(person);
    }
}
