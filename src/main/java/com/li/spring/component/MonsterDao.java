package com.li.spring.component;

import com.li.spring.annotation.Component;

/**
 * @author 李
 * @version 1.0
 */
@Component(value = "monsterDao")
public class MonsterDao {
    public void hi(){
        System.out.println("MonsterDao hi()被调用了");
    }
}
