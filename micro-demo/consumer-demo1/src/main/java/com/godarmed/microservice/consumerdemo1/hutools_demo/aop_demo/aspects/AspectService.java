package com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.aspects;

import java.lang.reflect.Method;

public interface AspectService {
    boolean before(Object var1, Method var2, Object[] var3);

    boolean after(Object var1, Method var2, Object[] var3, Object var4);

    boolean afterException(Object var1, Method var2, Object[] var3, Throwable var4);
}
