package com.godarmed.core.starters.feignwrapper.exception;

import com.godarmed.core.starters.system.exception.errors.BaseHandlerException;

public class FeignClientCreateException extends BaseHandlerException {
    private static final long serialVersionUID = 1L;

    public FeignClientCreateException(int code, String msg) {
        super(code, msg);
    }

    public FeignClientCreateException(String msg) {
        super(500, msg);
    }
}