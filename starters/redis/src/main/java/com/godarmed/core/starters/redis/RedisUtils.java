package com.godarmed.core.starters.redis;


import com.alibaba.fastjson.JSON;
import com.godarmed.core.starters.global.utils.SpringUtils;
import com.godarmed.core.starters.redis.entity.RedisConfigProperties;
import redis.clients.jedis.Jedis;

import java.util.*;

public class RedisUtils implements AutoCloseable {
    public static final String SET_SUCCESS = "OK";
    public static final Long EVAL_SUCCESS = 1L;
    public static final String SET_IF_NOT_EXIST = "NX";
    public static final String SET_WITH_EXPIRE_TIME = "EX";
    private Jedis jedis;
    private RedisCustConfig redisCustConfig;
    private RedisConfigProperties config;

    public RedisUtils() throws Exception {
        this.redisCustConfig = (RedisCustConfig) SpringUtils.getBean(RedisCustConfig.class);
        if (this.redisCustConfig != null) {
            this.config = this.redisCustConfig.getConfig();
            this.jedis = this.redisCustConfig.redisPoolFactory().getResource();
        } else {
            throw new Exception("jedis init error");
        }
    }

    public RedisUtils(RedisCustConfig redisCustConfig) throws Exception {
        if (redisCustConfig != null) {
            this.config = redisCustConfig.getConfig();
            this.jedis = redisCustConfig.redisPoolFactory().getResource();
        } else {
            throw new Exception("jedis init error");
        }
    }

    public <T> void set(String key, T value) {
        this.set(key, value, -1);
    }

    public <T> void set(String key, T value, int second) {
        this.jedis.setex(this.createKey(key), second > 0 ? second : this.config.getTtl().intValue() / 1000, JSON.toJSONString(value));
    }

    public String get(String key) {
        return this.exists(key) ? this.jedis.get(this.createKey(key)) : null;
    }

    public <T> List<T> getArray(String key, Class<T> type) {
        String value = this.get(key);
        return value != null ? JSON.parseArray(value, type) : null;
    }

    public <T> T get(String key, Class<T> type) {
        String value = this.get(key);
        return value != null ? JSON.parseObject(value, type) : null;
    }

    public boolean exists(String key) {
        return this.jedis.exists(this.createKey(key));
    }

    public Long addByKey(String key, Integer number) {
        return this.jedis.incrBy(this.createKey(key), (long) number);
    }

    public void remove(String key) {
        if (this.exists(key)) {
            this.jedis.del(this.createKey(key));
        }

    }

    public final String getLock(String name, Long lockTimeout, Long waitTime) throws Exception {
        Long getTimeout = System.currentTimeMillis() + waitTime;
        String acquired = null;
        String uuid = UUID.randomUUID().toString();

        do {
            if (System.currentTimeMillis() > getTimeout) {
                return null;
            }

            acquired = this.jedis.set(this.createKey(name), uuid, "NX", "EX", lockTimeout / 1000L);
            if ("OK".equals(acquired)) {
                return uuid;
            }

            Thread.sleep(10L);
        } while (System.currentTimeMillis() <= getTimeout);

        return null;
    }

    public final boolean releaseLock(String name, String lockFlag) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = this.jedis.eval(script, Collections.singletonList(this.createKey(name)), Collections.singletonList(lockFlag));
        return EVAL_SUCCESS.equals(result);
    }

    public final void deleteKeys(String pattern) {
        Set<String> keys = this.jedis.keys(pattern);
        Iterator var3 = keys.iterator();

        while (var3.hasNext()) {
            String key = (String) var3.next();
            this.jedis.del(key);
        }

    }

    private String createKey(String key) {
        return this.config.getCacheKeyPrefix().equals("") ? key : this.config.getCacheKeyPrefix() + "_" + key;
    }

    public void push(String key, String value) {
        this.jedis.lpush(this.createKey(key), new String[]{value});
    }

    public String pop(String key) {
        return this.jedis.rpop(this.createKey(key));
    }

    @Override
    public void close() {
        if (this.jedis != null) {
            this.jedis.close();
        }

    }

    public static void main(String[] args) throws Exception {
        RedisConfigProperties redisConfigProperties = new RedisConfigProperties();
        redisConfigProperties.setHost("192.168.1.166");
        redisConfigProperties.setPort(6379);
        redisConfigProperties.setCacheKeyPrefix("imusic-server-BTV01");
        redisConfigProperties.setTtl(180000L);
        RedisCustConfig redisCustConfig = new RedisCustConfig();
        redisCustConfig.setConfig(redisConfigProperties);
        RedisUtils redisUtils = new RedisUtils(redisCustConfig);
        String res = redisUtils.get("123456");
        System.out.println(res);
    }
}
