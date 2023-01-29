package com.li.spring.processor;

/**
 * @author 李
 * @version 1.0
 * 说明：
 * 1.根据 spring 原生机制定义了一个接口
 * 2.该接口有一个方法 afterPropertiesSet()，
 * 3.afterPropertiesSet() 在 bean的 setter方法后执行，相当于原生的spring的 bean初始化方法
 * 4.当一个Bean实现了这个接口后，就要实现afterPropertiesSet()，即 bean的初始化方法
 */
public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
