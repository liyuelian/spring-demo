package com.li.spring.component;

import com.li.spring.annotation.AutoWired;
import com.li.spring.annotation.Component;
import com.li.spring.annotation.Scope;
import com.li.spring.processor.InitializingBean;

/**
 * @author 李
 * @version 1.0
 * MonsterService 是一个 Service
 * 1.如果指定了value，那么在注入spring容器时，以你指定的为准
 * 2.如果没有指定value，则使用类名（首字母小写）作为默认名
 */
@Component//(value = "monsterService") //将 MonsterService注入到自己的spring容器中
@Scope(value = "prototype")
public class MonsterService implements InitializingBean {
    //使用自定义注解修饰属性，表示该属性通过容器完成依赖注入
    // （说明：按照名字进行组装）
    @AutoWired(required = true)
    private MonsterDao monsterDao;

    public void m1() {
        monsterDao.hi();
    }

    /**
     * 1.afterPropertiesSet()就是在bean的setter方法执行完毕之后，被spring容器调用
     * 2.即 bean的初始化方法
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("MonsterService 初始化方法被调用");
    }
}
