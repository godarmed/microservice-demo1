package com.godarmed.core.starters.redis.lock;

import com.godarmed.core.starters.redis.RedisCustConfig;
import com.godarmed.core.starters.redis.RedisUtils;
import com.google.common.base.Strings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public abstract class AbstractRedisLock {
    private static final Logger log = LogManager.getLogger(AbstractRedisLock.class);
    private RedisUtils redisUtils;
    private String lockValue;
    private static final Long DEFAULT_LOCK_TIMEOUT = 300000L;
    private static final Long DEFAULT_ACQUIRE_TIMEOUT = 300000L;
    private String lockName;
    public static final String LOCK_PREFIX = "LOCK";

    public AbstractRedisLock(String lockName) throws Exception {
        this(lockName, (RedisUtils)null);
    }

    public AbstractRedisLock(String lockName, RedisUtils redisUtils) throws Exception {
        this(lockName, redisUtils, (RedisCustConfig)null);
    }

    public AbstractRedisLock(String lockName, RedisUtils redisUtils, RedisCustConfig redisCustConfig) throws Exception {
        this.lockValue = null;
        this.lockName = null;
        if (Strings.isNullOrEmpty(lockName)) {
            throw new Exception("init lock error for lockname is empty");
        } else {
            if (log.isDebugEnabled()) {
                log.debug("AbstractRedisLock begin to get instance");
            }

            this.setLockName("LOCK_" + lockName);
            if (redisUtils != null) {
                this.redisUtils = redisUtils;
            } else if (redisCustConfig != null) {
                new RedisUtils(redisCustConfig);
            } else {
                new RedisUtils();
            }

            if (log.isDebugEnabled()) {
                log.debug("AbstractRedisLock has successfull instance");
            }

        }
    }

    public final boolean acquireLock(Long lockTimeout, Long acquireTimeout) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("acquireLock begin......");
            }

            this.lockValue = this.redisUtils.getLock(this.lockName, lockTimeout == null ? DEFAULT_LOCK_TIMEOUT : lockTimeout, acquireTimeout == null ? DEFAULT_ACQUIRE_TIMEOUT : acquireTimeout);
            if (this.lockValue != null) {
                if (log.isDebugEnabled()) {
                    log.debug("acquireLock end......true");
                }

                return true;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        if (log.isDebugEnabled()) {
            log.debug("acquireLock end......false");
        }

        return false;
    }

    public final boolean refreshLock(int lockTimeout) {
        if (this.lockValue != null) {
            try {
                this.redisUtils.set(this.lockName, this.lockValue, lockTimeout / 1000);
                return true;
            } catch (Exception var3) {
                var3.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public final boolean acquireLock() {
        return this.acquireLock((Long)null, (Long)null);
    }

    public final boolean releaseLock() {
        try {
            if (this.lockValue != null) {
                return this.redisUtils.releaseLock(this.lockName, this.lockValue);
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return false;
    }

    public final Boolean releaseLockAndClose() {
        Boolean result = this.releaseLock();
        this.close();
        return result;
    }

    public final void close() {
        this.lockValue = null;
        this.lockName = null;
        if (this.redisUtils != null) {
            this.redisUtils.close();
        }

    }

    public abstract void syncOperation(RedisUtils var1, Map<String, Object> var2);

    public final void sync(Long lockTimeout, Long acquireTimeout, Map<String, Object> context) {
        if (this.acquireLock(lockTimeout, acquireTimeout)) {
            try {
                this.syncOperation(this.redisUtils, context);
            } catch (Exception var8) {
                var8.printStackTrace();
            } finally {
                this.releaseLock();
            }
        }

    }

    public final void sync(Long lockTimeout, Long acquireTimeout) {
        if (this.acquireLock(lockTimeout, acquireTimeout)) {
            try {
                this.syncOperation(this.redisUtils, (Map)null);
            } catch (Exception var7) {
                var7.printStackTrace();
            } finally {
                this.releaseLock();
            }
        }

    }

    public final void sync() {
        this.sync((Long)null, (Long)null);
    }

    public final void sync(Map<String, Object> context) {
        this.sync((Long)null, (Long)null, context);
    }

    public String getLockName() {
        return this.lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }
}
