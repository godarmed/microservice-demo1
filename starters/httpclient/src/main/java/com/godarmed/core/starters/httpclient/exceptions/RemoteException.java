package com.godarmed.core.starters.httpclient.exceptions;

import com.godarmed.core.starters.system.exception.errors.BaseHandlerException;

public class RemoteException extends BaseHandlerException {
    private static final long serialVersionUID = 1L;

    public RemoteException(int code, String message) {
        super(code, message);
    }

    public RemoteException(RemoteExceptionEnum exception) {
        super(exception.getCode(), exception.getMessage());
    }

    public RemoteException(RemoteExceptionEnum exception, String message) {
        super(exception.getCode(), exception.getMessage() + "[" + message + "]");
    }
}
