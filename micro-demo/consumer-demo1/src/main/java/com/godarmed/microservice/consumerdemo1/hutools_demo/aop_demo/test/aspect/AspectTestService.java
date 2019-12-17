package com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.test.aspect;

import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.aspects.AspectService;
import com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.test.entity.ThrowExeceptionDTO;

import java.io.Serializable;
import java.lang.reflect.Method;

public class AspectTestService  implements AspectService, Serializable {
    private static final long serialVersionUID = 1L;

    public AspectTestService() {
    }

    @Override
    public boolean before(Object var1, Method var2, Object[] var3) {
        for (Object o : var3) {
            if(o instanceof ThrowExeceptionDTO){
                ((ThrowExeceptionDTO)o).throwBeforeException();
            }
        }
        System.out.println("运行前");
        return true;
    }

    @Override
    public boolean after(Object var1, Method var2, Object[] var3, Object var4) {
        for (Object o : var3) {
            if(o instanceof ThrowExeceptionDTO){
                ((ThrowExeceptionDTO)o).throwAfterException();
            }
        }
        System.out.println("运行后");
        return true;
    }

    @Override
    public boolean afterException(Object var1, Method var2, Object[] var3, Throwable var4) {
        System.out.println("运行异常"+var4.getMessage());
        return true;
    }
}
