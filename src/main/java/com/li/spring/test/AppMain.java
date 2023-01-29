package com.li.spring.test;

import com.li.spring.component.MonsterDao;
import com.li.spring.component.MonsterService;
import com.li.spring.ioc.MySpringApplicationContext;
import com.li.spring.ioc.MySpringConfig;

/**
 * @author 李
 * @version 1.0
 */
public class AppMain {
    public static void main(String[] args) {
        MySpringApplicationContext ioc =
                new MySpringApplicationContext(MySpringConfig.class);
        MonsterService monsterService = (MonsterService) ioc.getBean("monsterService");
        monsterService.m1();
    }
}
