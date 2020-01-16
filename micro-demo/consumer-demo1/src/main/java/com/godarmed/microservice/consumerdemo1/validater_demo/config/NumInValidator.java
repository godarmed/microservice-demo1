package com.godarmed.microservice.consumerdemo1.validater_demo.config;


import com.godarmed.core.starters.global.utils.DynamicEnumUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.assertj.core.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Log4j2
public class NumInValidator implements ConstraintValidator<NumIn,Integer> {
    @Override
    public void initialize(NumIn myConstraint) {
        resetNumEnum(myConstraint.enumValid(),Arrays.asList(1,3,5,7));
        //获取中注解中的方法
        System.out.println("NumIn validator init");
    }

    @Override
    public boolean isValid(Integer num, ConstraintValidatorContext constraintValidatorContext) {
        //使用枚举类校验
        return NumEnum.isInEnum(num);
    }

    public static <T extends Enum<?>> void resetNumEnum(Class<T> enumClass, List<Integer> sourceEnumValues) {
        //从数据库中获取枚举值
        //List<Integer> sourceEnumValues = Arrays.asList(1,3,5,7,9);
        //去重
        sourceEnumValues = sourceEnumValues.stream().distinct().collect(Collectors.toList());
        //创建枚举实例名称
        String[] enumNames = (String[]) sourceEnumValues.stream().map(item -> StringUtils.join("NUM_", item)).toArray();
        Object[][] enumValues = new Object[sourceEnumValues.size()][];
        for (int i = 0; i < sourceEnumValues.size(); i++) {
            enumValues[i] = new Object[]{sourceEnumValues.get(i)};
        }

        //设置到枚举项中
        DynamicEnumUtils.resetEnum(enumClass,enumNames,new Class[]{Integer.class},enumValues);

    }
}