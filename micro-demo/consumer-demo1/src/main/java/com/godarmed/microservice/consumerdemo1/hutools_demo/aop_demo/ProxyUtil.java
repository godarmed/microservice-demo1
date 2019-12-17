package com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo;

import cn.hutool.core.util.ClassUtil;
import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.aspects.AspectService;
import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyUtil {
    public ProxyUtil() {
    }

    public static <T> T proxy(T target, Class<? extends AspectService> aspectClass) {
        return ProxyFactory.createProxy(target, aspectClass);
    }

    public static <T> T proxy(T target, AspectService aspect) {
        return ProxyFactory.createProxy(target, aspect);
    }

    public static <T> T newProxyInstance(ClassLoader classloader, InvocationHandler invocationHandler, Class... interfaces) {
        return (T)Proxy.newProxyInstance(classloader, interfaces, invocationHandler);
    }

    public static <T> T newProxyInstance(InvocationHandler invocationHandler, Class... interfaces) {
        return newProxyInstance(ClassUtil.getClassLoader(), invocationHandler, interfaces);
    }
}
