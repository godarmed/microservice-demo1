package com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.aspects;

import java.io.Serializable;
import java.lang.reflect.Method;

public abstract class DefaultAspectService implements AspectService, Serializable {
    private static final long serialVersionUID = 1L;

    public DefaultAspectService() {
    }

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        return true;
    }

    public boolean after(Object target, Method method, Object[] args) {
        return this.after(target, method, args, (Object)null);
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        return true;
    }

    @Override
    public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
        return true;
    }
}
