package com.godarmed.microservice.consumerdemo1.common.redisLock.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {
    /**
     * 锁名称
     */
    String lockName() default "";

    /**
     * 锁过期时间
     */
    long lockTimeout() default 300L;


}
