package com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.aspects;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;

import java.lang.reflect.Method;

public class TimeIntervalAspect extends DefaultAspectService {
    private static final long serialVersionUID = 1L;
    private TimeInterval interval = new TimeInterval();

    public TimeIntervalAspect() {
    }

    @Override
    public boolean before(Object target, Method method, Object[] args) {
        this.interval.start();
        return true;
    }

    @Override
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        Console.log("Method [{}.{}] execute spend [{}]ms return value [{}]", new Object[]{target.getClass().getName(), method.getName(), this.interval.intervalMs(), returnVal});
        return true;
    }
}