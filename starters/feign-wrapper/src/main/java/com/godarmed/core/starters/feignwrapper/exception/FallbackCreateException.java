package com.godarmed.core.starters.feignwrapper.exception;

public class FallbackCreateException extends Exception {
    private static final long serialVersionUID = 1L;

    public FallbackCreateException(String code, String msg) {
        super("FallbackCreateException[" + code + "]:" + msg);
    }
}