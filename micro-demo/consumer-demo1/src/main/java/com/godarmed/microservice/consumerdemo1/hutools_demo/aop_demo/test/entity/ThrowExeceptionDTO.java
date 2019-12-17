package com.godarmed.microservice.consumerdemo1.hutools_demo.aop_demo.test.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ThrowExeceptionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    boolean beforeException;

    boolean runtimeException;

    boolean afterException;

    public ThrowExeceptionDTO() {
    }

    public ThrowExeceptionDTO(boolean beforeException, boolean runtimeException, boolean afterException) {
        this.beforeException = beforeException;
        this.runtimeException = runtimeException;
        this.afterException = afterException;
    }

    public void throwBeforeException(){
        if(this.beforeException){
            throw new RuntimeException("运行前异常");
        }

    }

    public void throwRuntimeException(){
        if(this.runtimeException) {
            throw new RuntimeException("运行中异常");
        }
    }

    public void throwAfterException(){
        if(this.afterException){
            throw new RuntimeException("运行后异常");
        }
    }

}
