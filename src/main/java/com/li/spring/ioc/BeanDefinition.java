package com.li.spring.ioc;

/**
 * @author 李
 * @version 1.0
 * 用于封装/记录 Bean对象的信息：
 * 1.scope
 * 2.Bean对应的 Class对象，用于反射生成对应对象
 */
public class BeanDefinition {
    private String scope;
    private Class clazz;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "scope='" + scope + '\'' +
                ", clazz=" + clazz +
                '}';
    }
}
