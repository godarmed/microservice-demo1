package com.godarmed.core.starters.httpclient.exceptions;

public enum RemoteExceptionEnum {
    /**
     *  未知异常
     */
    UNKNOWN_ERROR(100000, "未知异常");

    private int code = 0;
    private String message = null;

    private RemoteExceptionEnum(int code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

