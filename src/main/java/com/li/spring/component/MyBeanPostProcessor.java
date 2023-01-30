package com.li.spring.component;

import com.li.spring.annotation.Component;
import com.li.spring.processor.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("后置处理器MyBeanPostProcessor的 After()方法被调用，bean类型="
                + bean.getClass() + "，bean的名字=" + beanName);
        //实现aop，返回代理对象，即对bean进行包装
        //先写死，后面我们可以通过注解的方式更加灵活运用
        if ("smartDog".equals(beanName)) {//1.通过注解看看当前这个类是否已经被代理 [需要事先在Map中保存代理关系]
            //使用jdk的动态代理，返回bean的代理对象
            Object proxyInstance = Proxy.newProxyInstance(MyBeanPostProcessor.class.getClassLoader(),
                    bean.getClass().getInterfaces(), new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            System.out.println("method=" + method.getName());
                            Object result = null;
                            //假设我们要进行前置,返回通知处理的方法是getSum()
                            //（原生的spring的通知方法是通过注解来获取的）
                            //这里我们后面再通过注解来做得更加灵活
                            if ("getSum".equals(method.getName())) {//2.通过注解查看当前类的哪个方法已经被代理 [需要事先在Map中保存代理关系]
                                //前置通知
                                SmartAnimalAspect.showBeginLog();//3.通过@Before注解查看当前类的这个方法已经被切面类的哪个方法进行@Before切入 [需要事先在Map中保存代理关系]
                                //目标方法
                                result = method.invoke(bean, args);
                                //返回通知
                                SmartAnimalAspect.showSuccessLog();//4.通过@AfterReturning注解查看当前类的这个方法已经被切面类的哪个方法进行@AfterReturning切入 [需要事先在Map中保存代理关系]
                            } else {
                                result = method.invoke(bean, args);
                            }
                            return result;
                        }
                    });
            //如果bean需要返回代理对象，这里就直接return ProxyInstance
            return proxyInstance;
        }
        //如果不需要AOP，直接返回 bean
        return bean;
    }
}
