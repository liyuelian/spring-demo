package com.li.spring.ioc;

import com.li.spring.annotation.ComponentScan;

/**
 * @author 李
 * @version 1.0
 * 这是一个配置类，作用类似我们原生 spring 的容器配置文件 beans.xml
 */
@ComponentScan(value = "com.li.spring.component")
public class MySpringConfig {

}
