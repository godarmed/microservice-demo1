package com.godarmed.microservice.consumerdemo1.beanUtils_demo.controller;

import com.godarmed.core.starters.global.entity.ResultModel;
import com.godarmed.microservice.consumerdemo1.beanUtils_demo.entity.BeanUser;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validTest")
public class BeanUtilsController {
    public static void main(String[] args) {
        BeanUser source = new BeanUser();
        source.setName("aaa");
        source.setAddress("bbb");

        BeanUser target = new BeanUser();
        BeanUtils.copyProperties(source,target);
    }

    @RequestMapping("/getBeanUser")
    public ResultModel<BeanUser> getBeanUser(){
        ResultModel<BeanUser> ret = new ResultModel<>();
        BeanUser source = new BeanUser();
        source.setName("aaa");
        source.setAddress("bbb");
        ret.setData(source);
        return ret;
    }

}
