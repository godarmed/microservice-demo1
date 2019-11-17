package com.godarmed.microservice.consumerdemo1.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public CacheManager cacheManager(
            RedisTemplate<?, ?> redisTemplate) {
        // RedisCache需要一个RedisCacheWriter来实现读写Redis
        RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(redisTemplate.getConnectionFactory());
        // SerializationPair用于Java对象和Redis之间的序列化和反序列化
        // Spring Boot默认采用JdkSerializationRedisSerializer的二进制数据序列化方式
        // 使用该方式，保存在redis中的值是人类无法阅读的乱码，并且该Serializer要求目标类必须实现Serializable接口
        // 本示例中，使用StringRedisSerializer来序列化和反序列化redis的key值
        RedisSerializationContext.SerializationPair keySerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(
                new StringRedisSerializer());
        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisSerializationContext.SerializationPair valueSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(
                jackson2JsonRedisSerializer);
        // 构造一个RedisCache的配置对象，设置缓存过期时间和Key、Value的序列化机制
        // 这里设置缓存过期时间为1天
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5))
                .serializeKeysWith(keySerializationPair).serializeValuesWith(valueSerializationPair);
        return new RedisCacheManager(writer, config);
    }
}