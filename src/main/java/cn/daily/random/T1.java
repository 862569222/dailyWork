package cn.daily.random;

import org.junit.Test;

import java.util.Random;

/**
 * @author zhaibo
 * @title: zb
 * @projectName eureka
 * @description: TODO
 * @date 2021/3/10 7:01
 */
public class T1 {
    @Test
    public void t1(){
        double random = RandomNumberGeneratorHolder.randomNumberGenerator.nextDouble();
        System.out.println(RandomNumberGeneratorHolder.randomNumberGenerator);
        System.out.println(RandomNumberGeneratorHolder.randomNumberGenerator);
        System.out.println(RandomNumberGeneratorHolder.randomNumberGenerator);

        System.out.println(random);

    }

    private static final class RandomNumberGeneratorHolder {
        static final Random randomNumberGenerator = new Random();
    }
}
