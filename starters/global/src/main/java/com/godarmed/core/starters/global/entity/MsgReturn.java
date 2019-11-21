package com.godarmed.core.starters.global.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MsgReturn<T> implements Serializable {
    private static final long serialVersionUID = 9220082412259258844L;
    private int status = 200;
    private String msg = "success";
    private T data;
    private MsgPageInfo pageInfo;

    public MsgReturn() {
    }

    public MsgReturn<T> success() {
        this.setStatus(200);
        this.setMsg("success");
        return this;
    }

    public MsgReturn<T> success(T object) {
        this.setData(object);
        this.setStatus(200);
        this.setMsg("success");
        return this;
    }

    public MsgReturn<T> fail() {
        this.setStatus(400);
        this.setMsg("fail");
        return this;
    }

    public MsgReturn<T> fail(String errMsg) {
        this.setStatus(400);
        this.setMsg(errMsg);
        return this;
    }

    public void setData(T data, MsgPageInfo pageInfo) {
        this.data = data;
        this.pageInfo = pageInfo;
    }
}
