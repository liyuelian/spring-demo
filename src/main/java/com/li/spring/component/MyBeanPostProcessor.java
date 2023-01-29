package com.li.spring.component;

import com.li.spring.annotation.Component;
import com.li.spring.processor.BeanPostProcessor;

/**
 * @author 李
 * @version 1.0
 * 1.这是我们自己的一个后置处理器，它实现了我们自定义的 BeanPostProcessor
 * 2.通过重写 BeanPostProcessor的方法实现相应业务
 * 3.仍然将 MyBeanPostProcessor 看做一个Bean对象，使用Component注解，注入到spring容器中。
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("后置处理器MyBeanPostProcessor的 Before()方法被调用，bean类型="
                + bean.getClass() + "，bean的名字=" + beanName);
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("后置处理器MyBeanPostProcessor的 After()方法被调用，bean类型="
                + bean.getClass() + "，bean的名字=" + beanName);
        return null;
    }
}
