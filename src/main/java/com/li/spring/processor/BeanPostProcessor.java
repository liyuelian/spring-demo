package com.li.spring.processor;

/**
 * @author 李
 * @version 1.0
 * 说明：
 * 1.此接口参考原生Spring容器定义
 * 2.该接口有两个方法 postProcessBeforeInitialization和 postProcessAfterInitialization
 * 3.这两个方法会对spring容器的所有bean生效（切面编程）
 */
public interface BeanPostProcessor {

    /**
     * postProcessBeforeInitialization方法在bean的初始化方法前 调用
     *
     * @param bean
     * @param beanName
     * @return
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }


    /**
     * postProcessAfterInitialization方法在bean的初始化方法后 调用
     *
     * @param bean
     * @param beanName
     * @return
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
