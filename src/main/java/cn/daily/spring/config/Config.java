package cn.daily.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaibo
 * @title: zb
 * @projectName dailyWork
 * @description: TODO
 * @date 2020/12/27 21:35
 */
@Configuration
public class Config {

    @Conditional(ManCondition.class)//根据条件判断是否加载bean
    @Bean("person")
    public Person getBean(){
        System.out.println("张三实例话。。。。");
        return new Person("张三",23);
    }

    @Conditional(LinCondition.class)
    @Bean("anmial")
    public Tom getBeanTom(){
        System.out.println("TOM实例话。。。。");
        return new Tom();
    }
}
