package com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.proxy;

import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.aspects.AspectService;
import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.interceptor.CglibInterceptor;
import net.sf.cglib.proxy.Enhancer;

public class CglibProxyFactory extends ProxyFactory {
    private static final long serialVersionUID = 1L;

    public CglibProxyFactory() {
    }

    @Override
    public <T> T proxy(T target, AspectService aspect) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new CglibInterceptor(target, aspect));
        return (T)enhancer.create();
    }
}
