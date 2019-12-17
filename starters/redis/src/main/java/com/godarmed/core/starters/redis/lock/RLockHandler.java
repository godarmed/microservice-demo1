package com.godarmed.core.starters.redis.lock;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class RLockHandler {
    private RedissonClient client = null;
    private static final String LOCK_PREFIX = "Redisson_esky_lock_";

    public RLockHandler() {
        this.client = Redisson.create();
    }

    public RLockHandler(RedissonClient client) {
        this.client = client;
    }

    public boolean acquire(String key, Long expireSeconds,Long waitTime) throws InterruptedException {
        RLock lock = this.client.getLock(getRealKey(key));
        return lock.tryLock(waitTime,expireSeconds, TimeUnit.SECONDS);
    }

    public boolean release(String key) {
        RLock lock = this.client.getLock(getRealKey(key));
        lock.unlock();
        return true;
    }

    public static String getRealKey(String key) {
        return "Redisson_esky_lock_" + key;
    }
}
