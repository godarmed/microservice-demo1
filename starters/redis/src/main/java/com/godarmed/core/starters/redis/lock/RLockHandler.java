package com.godarmed.core.starters.redis.lock;

import java.util.concurrent.TimeUnit;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class RLockHandler {
    private static RedissonClient client = null;
    private static final String LOCK_PREFIX = "Redisson_esky_lock_";

    public RLockHandler(RedissonClient client) {
        client = client;
    }

    public static boolean acquire(String key, int expireSeconds) {
        if (client == null) {
            return false;
        } else {
            RLock lock = client.getLock(getRealKey(key));
            lock.lock((long)expireSeconds, TimeUnit.SECONDS);
            return true;
        }
    }

    public static boolean release(String key) {
        RLock lock = client.getLock(getRealKey(key));
        lock.unlock();
        return true;
    }

    public static String getRealKey(String key) {
        return "Redisson_esky_lock_" + key;
    }
}
