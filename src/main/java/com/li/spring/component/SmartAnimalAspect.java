package com.li.spring.component;

/**
 * @author 李
 * @version 1.0
 * 先将SmartAnimalAspect 当做一个切面类来使用
 * 后面再分析如何做得更加灵活
 */
public class SmartAnimalAspect {
    public static void showBeginLog() {
        System.out.println("前置通知...");
    }

    public static void showSuccessLog() {
        System.out.println("返回通知...");
    }
}
