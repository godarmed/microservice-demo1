package com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.test.service;

import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.ProxyUtil;
import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.test.aspect.AspectTestService;
import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.test.entity.ThrowExeceptionDTO;

public class TestService {

    public String task(ThrowExeceptionDTO request){
        request.throwRuntimeException();
        System.out.println("运行中");
        return "运行中";
    }

    public static void main(String[] args) {
        ThrowExeceptionDTO throwExeceptionDTO = new ThrowExeceptionDTO(false,false,true);

        TestService testService = ProxyUtil.proxy(new TestService(), AspectTestService.class);
        testService.task(throwExeceptionDTO);
    }

}
