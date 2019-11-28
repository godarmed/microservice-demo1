package com.godarmed.core.starters.redis.limit.enums;

public enum LimitType {
    //请求用户
    CUSTOMER,
    //请求IP
    IP;

    private LimitType() {
    }
}