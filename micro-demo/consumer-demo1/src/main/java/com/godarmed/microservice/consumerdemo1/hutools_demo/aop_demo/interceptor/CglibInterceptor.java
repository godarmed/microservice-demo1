package com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.interceptor;

import cn.hutool.aop.aspects.Aspect;
import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.aspects.AspectService;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CglibInterceptor implements MethodInterceptor, Serializable {
    private static final long serialVersionUID = 1L;
    private final Object target;
    private final AspectService aspect;

    public CglibInterceptor(Object target, AspectService aspect) {
        this.target = target;
        this.aspect = aspect;
    }

    public Object getTarget() {
        return this.target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = null;
        if (this.aspect.before(this.target, method, args)) {
            try {
                result = proxy.invokeSuper(obj, args);
            } catch (InvocationTargetException var7) {
                if (this.aspect.afterException(this.target, method, args, var7.getTargetException())) {
                    throw var7;
                }
            }
        }

        return this.aspect.after(this.target, method, args, result) ? result : null;
    }
}
