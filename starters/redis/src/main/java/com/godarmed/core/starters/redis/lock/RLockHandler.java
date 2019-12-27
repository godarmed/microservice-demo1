package com.godarmed.core.starters.redis.lock;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class RLockHandler {
    private static RedissonClient client = null;
    private static final String LOCK_PREFIX = "Redisson_leo_lock_";

    public RLockHandler(RedissonClient client) {
        RLockHandler.client = client;
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

    public static RLock lock(String key, int expireSeconds) {
        if (client == null) {
            return null;
        } else {
            RLock lock = null;
            try {
                lock = client.getLock(getRealKey(key));
                if (lock.tryLock((long)expireSeconds, TimeUnit.SECONDS)) {
                    if (!Thread.currentThread().isInterrupted()) {
                        return lock;
                    }
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return null;
        }
    }

    public static void release(RLock lock) {
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }

    }

    public static boolean release(String key) {
        RLock lock = client.getLock(getRealKey(key));
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }

        return true;
    }

    public static String getRealKey(String key) {
        return "Redisson_leo_lock_" + key;
    }
}
