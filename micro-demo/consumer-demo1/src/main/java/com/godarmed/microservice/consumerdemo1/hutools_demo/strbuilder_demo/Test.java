package com.godarmed.microservice.consumerdemo1.hutools_demo.strbuilder_demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.StrBuilder;

public class Test {
    public static void main(String[] args) {
        //StringBuilder
        TimeInterval timer = DateUtil.timer();
        StringBuilder b2 = new StringBuilder();
        for(int i =0; i< 10000000; i++) {
            b2.delete(0,b2.length());
            b2.append("test");
        }
        System.out.println(b2);
        Console.log(timer.interval());

        //StrBuilder
        TimeInterval timer2 = DateUtil.timer();
        StrBuilder builder = StrBuilder.create();
        for(int j =0; j < 10000000; j++) {
            builder.reset();
            builder.append("test");

        }
        System.out.println(builder);
        Console.log(timer2.interval());
    }
}
