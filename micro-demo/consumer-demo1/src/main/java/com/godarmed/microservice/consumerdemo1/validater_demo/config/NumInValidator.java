package com.godarmed.microservice.consumerdemo1.validater_demo.config;

import org.apache.poi.ss.formula.functions.T;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.function.Function;


public class NumInValidator implements ConstraintValidator<NumIn,Object> {
    @Override
    public void initialize(NumIn myConstraint) {
        //获取中注解中的方法
        try {
            myConstraint.methodClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("my validator init");
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        //这里写判断逻辑
        System.out.println(o);
        //我这里直接方法false，就是要提示错误，如果返回true就表示验证通过
        return false;
    }

    private Function<Object, List<Integer>>  processor = new Function<Object, List<Integer>>() {
        @Override
        public List<Integer> apply(Object in) {

            return null;

        }
    };

}