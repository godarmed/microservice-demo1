package com.godarmed.core.starters.redis.entity;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "spring.redis"
)
public class RedisConfigProperties extends GenericObjectPoolConfig {
    private String host;
    private int port;
    private int timeout;
    private String password;
    private int database;
    private Long ttl = 0L;
    private String sessionKeyPrefix = "spring";
    private String cacheKeyPrefix = "";
    private boolean isCluster = false;

    public RedisConfigProperties() {
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getDatabase() {
        return this.database;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }

    public Long getTtl() {
        return this.ttl;
    }

    public void setSessionKeyPrefix(String sessionKeyPrefix) {
        this.sessionKeyPrefix = sessionKeyPrefix;
    }

    public String getSessionKeyPrefix() {
        return this.sessionKeyPrefix;
    }

    public void setCacheKeyPrefix(String cacheKeyPrefix) {
        this.cacheKeyPrefix = cacheKeyPrefix;
    }

    public String getCacheKeyPrefix() {
        return this.cacheKeyPrefix;
    }

    public void setCluster(boolean isCluster) {
        this.isCluster = isCluster;
    }

    public boolean isCluster() {
        return this.isCluster;
    }
}
