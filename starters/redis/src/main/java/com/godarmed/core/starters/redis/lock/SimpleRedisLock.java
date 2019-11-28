package com.godarmed.core.starters.redis.lock;

import com.godarmed.core.starters.redis.RedisCustConfig;
import com.godarmed.core.starters.redis.RedisUtils;

import java.util.Map;

public class SimpleRedisLock extends AbstractRedisLock {
    public SimpleRedisLock(String lockName) throws Exception {
        super(lockName);
    }

    public SimpleRedisLock(String lockName, RedisUtils redisUtils) throws Exception {
        super(lockName, redisUtils);
    }

    public SimpleRedisLock(String lockName, RedisUtils redisUtils, RedisCustConfig redisCustConfig) throws Exception {
        super(lockName, redisUtils, redisCustConfig);
    }

    @Override
    public void syncOperation(RedisUtils redisUtils, Map<String, Object> context) {
    }
}

