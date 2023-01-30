package com.li.spring.component;

import com.li.spring.annotation.*;

/**
 * @author 李
 * @version 1.0
 * 先将SmartAnimalAspect 当做一个切面类来使用
 * 后面再分析如何做得更加灵活
 */
@Aspect
@Component
public class SmartAnimalAspect {
    @Before(value = "execution com.li.spring.component.SmartDog getSum")
    public static void showBeginLog() {
        System.out.println("前置通知...");
    }

    @AfterReturning(value = "execution com.li.spring.component.SmartDog getSum")
    public static void showSuccessLog() {
        System.out.println("返回通知...");
    }

    @After(value = "execution com.li.spring.component.SmartDog getSum")
    public static void showFinallyLog() {
        System.out.println("最终通知...");
    }
}
