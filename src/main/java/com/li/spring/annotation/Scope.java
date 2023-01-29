package com.li.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 李
 * @version 1.0
 * Scope 用于指定 bean的作用范围 [singleton/prototype]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    //通过 value 指定 bean 是 singleton 或 prototype
    String value() default "";
}
