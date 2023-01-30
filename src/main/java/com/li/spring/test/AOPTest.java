package com.li.spring.test;

import com.li.spring.annotation.After;
import com.li.spring.annotation.AfterReturning;
import com.li.spring.annotation.Before;
import com.li.spring.component.SmartAnimalAspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 李
 * @version 1.0
 */
public class AOPTest {
    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        //1.获取切面类的class对象
        Class<SmartAnimalAspect> smartAnimalAspectClass = SmartAnimalAspect.class;
        //2.遍历该类的所有方法
        for (Method declaredMethod : smartAnimalAspectClass.getDeclaredMethods()) {
            //3.如果切面类的方法有 @Before注解
            if (declaredMethod.isAnnotationPresent(Before.class)) {
                //4.得到该切入方法
                System.out.println("方法名=" + declaredMethod.getName());
                //得到注解的 value值
                Before annotation = declaredMethod.getAnnotation(Before.class);
                System.out.println("注解 value=" + annotation.value());
                //5.当前的方法就是要执行的方法
                Method declaredMethod1 = smartAnimalAspectClass.getDeclaredMethod(declaredMethod.getName());
                //6.反射调用切入方法
                declaredMethod1.invoke(smartAnimalAspectClass.newInstance(), null);
            } else if (declaredMethod.isAnnotationPresent(AfterReturning.class)) {
                //如果切面类的方法有 @AfterReturning 注解
                //逻辑和 if分支相同
                System.out.println("方法名=" + declaredMethod.getName());
                AfterReturning annotation = declaredMethod.getAnnotation(AfterReturning.class);
                System.out.println("注解 value=" + annotation.value());
                Method declaredMethod1 = smartAnimalAspectClass.getDeclaredMethod(declaredMethod.getName());
                declaredMethod1.invoke(smartAnimalAspectClass.newInstance(), null);

            } else if (declaredMethod.isAnnotationPresent(After.class)) {
                //如果切面类的方法有 @After 注解
                //逻辑和 if分支相同
                System.out.println("方法名=" + declaredMethod.getName());
                After annotation = declaredMethod.getAnnotation(After.class);
                System.out.println("注解 value=" + annotation.value());
                Method declaredMethod1 = smartAnimalAspectClass.getDeclaredMethod(declaredMethod.getName());
                declaredMethod1.invoke(smartAnimalAspectClass.newInstance(), null);
            }
        }
    }
}
