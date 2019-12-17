package com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.proxy;


import cn.hutool.aop.ProxyUtil;
import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.aspects.AspectService;
import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.interceptor.JdkInterceptor;

public class JdkProxyFactory extends ProxyFactory {
    private static final long serialVersionUID = 1L;

    public JdkProxyFactory() {
    }

    @Override
    public <T> T proxy(T target, AspectService aspect) {
        return ProxyUtil.newProxyInstance(target.getClass().getClassLoader(), new JdkInterceptor(target, aspect), target.getClass().getInterfaces());
    }
}
