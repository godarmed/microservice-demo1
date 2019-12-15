package com.godarmed.core.starters.redis.lock.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {
    /**
     * 锁名称
     */
    String lockName();

    /**
     * 锁过期时间
     */
    long lockTimeout() default 1000L;

    /**
     * 获取锁的等待时间
     */
    long waitTimeout() default 1000L;


}
