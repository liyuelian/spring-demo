package com.li.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 李
 * @version 1.0
 * 模仿spring原生注解，自定义一个注解
 * 1. @Target(ElementType.TYPE) 指定ComponentScan注解可以修饰TYPE元素
 * 2. @Retention(RetentionPolicy.RUNTIME) 指定ComponentScan注解 的保留范围
 * 3. String value() default "";  表示 ComponentScan 可以传入一个value值
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentScan {
    //通过value指定要扫描的包
    String value() default "";
}
