package com.godarmed.core.starters.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.godarmed.core.starters.redis.entity.RedisConfigProperties;
import com.godarmed.core.starters.redis.lock.RLockHandler;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import redis.clients.jedis.JedisPool;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.lang.reflect.Method;
import java.time.Duration;

@Configuration
@EnableConfigurationProperties({RedisConfigProperties.class})
@EnableCaching
@EnableRedisHttpSession(
        redisNamespace = "${spring.redis.session-key-prefix:spring}"
)
public class RedisCustConfig extends CachingConfigurerSupport {
    @Autowired
    private RedisConfigProperties redisConfigProperties;

    public RedisCustConfig() {
    }

    public RedisConfigProperties getConfig() {
        return this.redisConfigProperties;
    }

    public void setConfig(RedisConfigProperties redisConfigProperties) {
        this.redisConfigProperties = redisConfigProperties;
    }

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPool jedisPool = new JedisPool(this.redisConfigProperties, this.redisConfigProperties.getHost(), this.redisConfigProperties.getPort(), 0, this.redisConfigProperties.getTtl().intValue(), this.redisConfigProperties.getPassword(), this.redisConfigProperties.getDatabase(), (String) null, false, (SSLSocketFactory) null, (SSLParameters) null, (HostnameVerifier) null);
        return jedisPool;
    }

    @Bean
    public RedissonClient getRedisson() {
        Config config = new Config();
        if (this.redisConfigProperties.isCluster()) {
            ((ClusterServersConfig) config.useClusterServers().addNodeAddress(new String[]{"redis://" + this.redisConfigProperties.getHost() + ":" + this.redisConfigProperties.getPort()}).setPassword(this.redisConfigProperties.getPassword())).setTimeout(this.redisConfigProperties.getTtl().intValue());
        } else {
            ((SingleServerConfig) config.useSingleServer().setAddress("redis://" + this.redisConfigProperties.getHost() + ":" + this.redisConfigProperties.getPort()).setPassword(this.redisConfigProperties.getPassword())).setTimeout(this.redisConfigProperties.getTtl().intValue());
        }

        return Redisson.create(config);
    }

    @Bean
    public RLockHandler initLockHandler() {
        return new RLockHandler(this.getRedisson());
    }

    @Bean
    @Primary
    public RedisCacheManager cacheManager(RedisConnectionFactory cf) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().prefixKeysWith(this.redisConfigProperties.getCacheKeyPrefix() + "-").entryTtl(Duration.ofMillis(this.redisConfigProperties.getTtl())).disableCachingNullValues();
        return RedisCacheManager.builder(cf).cacheDefaults(config).build();
    }

    @Bean(
            name = {"cacheManagerForPermanent"}
    )
    public RedisCacheManager cacheManagerForPermanent(RedisConnectionFactory cf) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().prefixKeysWith(this.redisConfigProperties.getCacheKeyPrefix() + "-").entryTtl(Duration.ofMillis(86400000L)).disableCachingNullValues();
        return RedisCacheManager.builder(cf).cacheDefaults(config).build();
    }

    @Bean(
            name = {"jpaKeyGenerator"}
    )
    public KeyGenerator jpaKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object arg0, Method arg1, Object... arg2) {
                StringBuilder sb = new StringBuilder();
                sb.append(arg0.getClass().getSimpleName());
                sb.append(arg1.getName());
                Object[] var5 = arg2;
                int var6 = arg2.length;

                for (int var7 = 0; var7 < var6; ++var7) {
                    Object object = var5[var7];
                    sb.append(object.toString());
                }

                return sb.toString();
            }
        };
    }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append("::" + method.getName() + ":");
            Object[] var4 = objects;
            int var5 = objects.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                Object obj = var4[var6];
                sb.append(obj.toString());
            }

            return sb.toString();
        };
    }

    @Bean({"stringRedisTemplate"})
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
        om.enableDefaultTyping(DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);
        template.setValueSerializer(jacksonSeial);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();
        return template;
    }
}
