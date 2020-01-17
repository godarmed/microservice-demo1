package com.godarmed.microservice.consumerdemo1.validater_demo.config;


import lombok.extern.log4j.Log4j2;
import org.springframework.core.NamedThreadLocal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.stream.Collectors;


@Log4j2
public class NumInValidator implements ConstraintValidator<NumIn,Integer> {

    private static Map<String,List<Integer>> numArray = new HashMap<>();

    private static Map<String,Boolean> useDefaultNums = new HashMap<>();

    private ThreadLocal<String> currentName = new NamedThreadLocal<>("当前使用的限定数组名称");

    /**
     * 设置是否使用默认数组
     * @param isUseDefaultNums
     */
    public static void setUseDefaultNums(String validName,Boolean isUseDefaultNums){
        if(!numArray.containsKey(validName)){
            throw new RuntimeException("限定数组名称不能为空");
        }
        if(isUseDefaultNums == null){
            throw new RuntimeException("是否使用默认限定数组不能为NULL");
        }

        useDefaultNums.put(validName,isUseDefaultNums);
    }

    /**
     * 重置限定数组
     * @param num
     */
    public static void resetNumArray(String validName, List<Integer> num){
        if(!numArray.containsKey(validName)){
            throw new RuntimeException("限定数组名称不能为空");
        }
        if(num == null || num.size() == 0){
            throw new RuntimeException("限定数组不能为空");
        }
        setUseDefaultNums(validName,true);
        numArray.put(validName,num);
    }

    /**
     * 初始化方法,仅第一次调用时使用
     * @param myConstraint
     */
    @Override
    public void initialize(NumIn myConstraint) {
        currentName.set(myConstraint.name());
        log.info("初始化方法，当前线程[{}],当前限定组名称[{}]",Thread.currentThread().getName(),currentName.get());
        numArray.put(currentName.get(),Arrays.stream(myConstraint.nums()).boxed().collect(Collectors.toList()));

        System.out.println("NumIn validator init");
    }

    /**
     * 校验方法
     * @param num
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Integer num, ConstraintValidatorContext constraintValidatorContext) {
        log.info("校验方法，当前线程[{}],当前限定组名称[{}]",Thread.currentThread().getName(),currentName.get());
        for (Integer i : numArray.get(currentName.get())) {
            if(i.equals(num)){
                return true;
            }
        }
        return false;
    }
}