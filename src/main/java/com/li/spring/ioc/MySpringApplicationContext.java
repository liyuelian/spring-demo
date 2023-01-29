package com.li.spring.ioc;

import com.li.spring.annotation.AutoWired;
import com.li.spring.annotation.Component;
import com.li.spring.annotation.ComponentScan;
import com.li.spring.annotation.Scope;
import com.li.spring.processor.BeanPostProcessor;
import com.li.spring.processor.InitializingBean;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 李
 * @version 1.0
 * MySpringApplicationContext 类的作用类似Spring原生的ioc容器
 */
public class MySpringApplicationContext {
    private Class configClass;
    //定义属性 BeanDefinitionMap->存放BeanDefinition对象
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    //定义属性 SingletonObjects->存放单例对象
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    //定义属性 beanPostProcessorList，用于存放后置处理器
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();


    //构造器
    public MySpringApplicationContext(Class configClass) {
        beanDefinitionByScan(configClass);
        //后期封装成方法---------
        Enumeration<String> keys = beanDefinitionMap.keys();
        //遍历
        while (keys.hasMoreElements()) {
            //得到 beanName
            String beanName = keys.nextElement();
            //通过beanName得到对应的 beanDefinition 对象
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            //判断该 bean 是单例还是多例
            if ("singleton".equals(beanDefinition.getScope())) {
                //将该bean实例放入到singletonObjects中
                singletonObjects.put(beanName, createBean(beanName, beanDefinition));
            }
        }
        //System.out.println("singletonObjects 单例池=" + singletonObjects);
        //------------
        //System.out.println("beanDefinitionMap=" + beanDefinitionMap);
    }

    //该方法完成对指定包的扫描，并将Bean信息封装到BeanDefinition对象，再放入map中
    public void beanDefinitionByScan(Class configClass) {
        this.configClass = configClass;
        //步骤一：获取要扫描的包
        //1.先得到 MySpringConfig配置类的注解 @ComponentScan(value = "com.li.spring.component")
        ComponentScan componentScan =
                (ComponentScan) this.configClass.getDeclaredAnnotation(ComponentScan.class);
        //2.通过 componentScan的 value=>得到要扫描的包路径
        String path = componentScan.value();
        System.out.println("要扫描的包=" + path);

        //步骤二：得到要扫描的包下的所有资源（类.class）
        //1.得到类的加载器-->App 类加载器
        ClassLoader classLoader = MySpringApplicationContext.class.getClassLoader();
        //2.通过类的加载器获取到要扫描的包的资源 url=>类似一个路径
        path = path.replace(".", "/");//将原先路径的.替换成/ ==> com/li/component
        URL resource = classLoader.getResource(path);
        //resource=file:/D:/IDEA-workspace/spring/out/production/spring/com/li/component
        System.out.println("resource=" + resource);
        //3.将要加载的资源（.class）路径下的文件进行遍历
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();//将当前目录下的所有文件放到files数组中（这里没有实现递归）
            for (File f : files) {
                //System.out.println("============");
                //System.out.println("AbsolutePath=" + f.getAbsolutePath());
                //获取文件的绝对路径
                String fileAbsolutePath = f.getAbsolutePath();

                //只处理.class文件
                if (fileAbsolutePath.endsWith(".class")) {

                    //步骤三：获取全类名反射对象，并放入容器中
                    //将其转变为 com.li.spring.component.MyComponent.class 形式
                    //1.先获取到类名
                    String className = fileAbsolutePath.substring(
                            fileAbsolutePath.lastIndexOf("\\") + 1,
                            fileAbsolutePath.indexOf(".class"));
                    //2.获取类的完整路径（全类名）
                    // path.replace("/", ".") => com.li.component
                    String classFullName = path.replace("/", ".") + "." + className;
                    //3.判断该class文件是否要注入到容器中（该类是否有特定注解）
                    try {
                        /*
                        得到该类的Class对象：
                        （1）Class.forName(className) 可以反射加载类
                        （2）classLoader.loadClass(className)也可以反射类的Class
                        主要区别是：（1）的方式会调用该类的静态方法，（2）的方法不会
                         */
                        //因为这里只是要判断该类有没有注解，因此使用比较轻量级的方式
                        Class<?> clazz = classLoader.loadClass(classFullName);
                        //判断该类是否有特定注解
                        if (clazz.isAnnotationPresent(Component.class)) {
                            //如果该类使用了 @Component ,说明是spring bean
                            System.out.println("是一个spring bean=" + clazz + " 类名=" + className);

                            /*
                              为了方便，如果发现是一个后置处理器，将其放入到ArrayList集合中
                              1.在原生的spring容器中，对后置处理器还是走的getBean，createBean方法
                              2.如果这样做，需要在singletonObjects中加入相应业务逻辑
                              3.这里为了方便，我们简化处理
                             */
                            //判断当前class有没有实现接口
                            //注意这里我们不能通过 instanceof 判断 class是否实现了 BeanPostProcessor
                            //因为clazz不是一个实例对象，而是一个Class对象，需要如下判断：
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                BeanPostProcessor beanPostProcessor =
                                        (BeanPostProcessor) clazz.newInstance();
                                //放入ArrayList集合中
                                beanPostProcessorList.add(beanPostProcessor);
                                continue;//简化的方式，不再将后置处理器的bean信息放到 beanDefinition对象中
                            }

                            //-----------------------------------------
                            //得到 BeanName-key
                            //1.得到 Component 注解
                            Component componentAnnotation = clazz.getDeclaredAnnotation(Component.class);
                            //2.得到Component注解的value值
                            String beanName = componentAnnotation.value();
                            //如果没有指定，就使用类名（首字母小写）作为beanName
                            if ("".equals(beanName)) {
                                beanName = StringUtils.uncapitalize(className);
                            }
                            //将 Bean信息封装到 BeanDefinition对象-value
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setClazz(clazz);
                            //1.获取scope
                            if (clazz.isAnnotationPresent(Scope.class)) {
                                //如果配置了Scope,就设置配置的值
                                Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                                beanDefinition.setScope(scopeAnnotation.value());
                            } else {
                                //如果没有配置Scope，就设置默认值singleton
                                beanDefinition.setScope("singleton");
                            }

                            //将beanDefinition对象放入Map中
                            beanDefinitionMap.put(beanName, beanDefinition);
                            //--------------------------------------------
                        } else {
                            //如果没有使用，则说明不是spring bean
                            System.out.println("不是一个 spring bean=" + clazz + " 类名=" + className);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("===========================");
            }
        }
    }

    //完成createBean(BeanDefinition)方法
    public Object createBean(String beanName, BeanDefinition beanDefinition) {
        //得到Bean的class对象
        Class clazz = beanDefinition.getClazz();
        try {
            //反射创建bean实例
            Object instance = clazz.getDeclaredConstructor().newInstance();
            //todo 这里要加入依赖注入的业务逻辑
            //1.遍历当前要创建对象的所有属性字段
            for (Field declaredField : clazz.getDeclaredFields()) {
                //2.判断字段是否有AutoWired注解
                if (declaredField.isAnnotationPresent(AutoWired.class)) {
                    //判断是否需要自动装配
                    AutoWired autoWiredAnnotation =
                            declaredField.getAnnotation(AutoWired.class);
                    if (autoWiredAnnotation.required()) {
                        //3.得到字段的名称
                        String name = declaredField.getName();
                        //4.通过getBean()方法获取要组装的对象
                        //如果name对应的对象时单例的，就到单例池去获取，如果是多例的，就反射创建并返回
                        Object bean = getBean(name);
                        //5.进行组装
                        //暴破
                        declaredField.setAccessible(true);
                        declaredField.set(instance, bean);
                    }
                }
            }
            System.out.println("======已创建好实例=====" + instance);

            //todo 在bean的初始化方法前调用后置处理器的before方法
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                //在后置处理器的before方法，可以对容器的bean进行处理
                // 然后返回处理后的bean实例。相当于做了前置处理
                Object current = beanPostProcessor.
                        postProcessBeforeInitialization(instance, beanName);
                if (current != null) {
                    instance = current;
                }
            }

            //判断是否要执行bean的初始化方法
            // 1.判断当前创建的 bean对象是否实现了 InitializingBean
            // 2.instanceof 表示判断对象的运行类型 是不是 某个类型或某个类型的子类型
            if (instance instanceof InitializingBean) {
                //3.将instance转成接口类型，调用初始化方法
                try {
                    ((InitializingBean) instance).afterPropertiesSet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //todo 在bean的初始化方法后调用后置处理器的 after方法
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                //在后置处理器的 after方法，可以对容器的 bean进行处理
                // 然后返回处理后的bean实例。相当于做了后置处理
                Object current = beanPostProcessor.
                        postProcessAfterInitialization(instance, beanName);
                if (current != null) {
                    instance = current;
                }
            }

            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //如果反射对象失败
        return null;
    }

    //编写方法，返回容器对象
    public Object getBean(String name) {
        //传入的beanName是否在 beanDefinitionMap中存在
        if (beanDefinitionMap.containsKey(name)) {//存在
            //从 beanDefinitionMap中获取 beanDefinition对象
            BeanDefinition beanDefinition = beanDefinitionMap.get(name);
            if ("singleton".equalsIgnoreCase(beanDefinition.getScope())) {
                //如果是单例 bean，直接从单例池获取
                return singletonObjects.get(name);
            } else {//如果不是单例，调用createBean()，反射创建对象
                return createBean(name, beanDefinition);
            }
        } else {//不存在
            //抛出空指针异常
            throw new NullPointerException("不存在该bean=" + name);
        }
    }
}
