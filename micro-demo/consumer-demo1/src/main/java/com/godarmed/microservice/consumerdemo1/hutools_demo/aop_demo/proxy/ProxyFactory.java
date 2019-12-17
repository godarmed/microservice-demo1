package com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.proxy;

import cn.hutool.core.util.ReflectUtil;
import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.aspects.AspectService;

import java.io.Serializable;

public abstract class ProxyFactory implements Serializable {
    private static final long serialVersionUID = 1L;

    public ProxyFactory() {
    }

    public abstract <T> T proxy(T var1, AspectService var2);

    public static <T> T createProxy(T target, Class<? extends AspectService> aspectClass) {
        return createProxy(target, ReflectUtil.newInstance(aspectClass, new Object[0]));
    }

    public static <T> T createProxy(T target, AspectService aspect) {
        return create().proxy(target, aspect);
    }

    public static ProxyFactory create() {
        try {
            return new CglibProxyFactory();
        } catch (NoClassDefFoundError var1) {
            return new JdkProxyFactory();
        }
    }
}