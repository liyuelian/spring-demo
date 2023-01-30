package com.li.spring.component;

import com.li.spring.annotation.Component;

/**
 * @author 李
 * @version 1.0
 */
@Component(value = "smartDog")
public class SmartDog implements SmartAnimal {
    @Override
    public float getSum(float i, float j) {
        float res = i + j;
        System.out.println("SmartDog-getSum()-res=" + res);
        return res;
    }

    @Override
    public float getSub(float i, float j) {
        float res = i - j;
        System.out.println("SmartDog-getSub()-res=" + res);
        return res;
    }
}
