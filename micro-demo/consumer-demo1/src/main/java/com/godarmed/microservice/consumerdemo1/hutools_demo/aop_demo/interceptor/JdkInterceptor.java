package com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.interceptor;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.aspects.AspectService;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JdkInterceptor implements InvocationHandler, Serializable {
    private static final long serialVersionUID = 1L;
    private Object target;
    private AspectService aspect;

    public JdkInterceptor(Object target, AspectService aspect) {
        this.target = target;
        this.aspect = aspect;
    }

    public Object getTarget() {
        return this.target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = this.target;
        AspectService aspect = this.aspect;
        Object result = null;
        if (aspect.before(target, method, args)) {
            ReflectUtil.setAccessible(method);

            try {
                result = method.invoke(ClassUtil.isStatic(method) ? null : target, args);
            } catch (InvocationTargetException var8) {
                if (aspect.afterException(target, method, args, var8.getTargetException())) {
                    throw var8;
                }
            }
        }

        return aspect.after(target, method, args, result) ? result : null;
    }
}
