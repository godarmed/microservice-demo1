package com.godarmed.core.starters.redis.limit;

import com.godarmed.core.starters.redis.limit.enums.LimitType;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;

@Component
@Aspect
public class LimitInterceptor {
    private static final Logger log = LogManager.getLogger(LimitInterceptor.class);
    private final RedisTemplate<String, Serializable> limitRedisTemplate;
    private static final String UNKNOWN = "unknown";

    @Autowired
    public LimitInterceptor(RedisTemplate<String, Serializable> limitRedisTemplate) {
        this.limitRedisTemplate = limitRedisTemplate;
    }

    @Pointcut("@annotation(com.godarmed.core.starters.redis.limit.Limit)")
    public void limitPointCut() {
    }

    @Around("limitPointCut() && @annotation(limitAnnotation)")
    public Object interceptor(ProceedingJoinPoint pjp, Limit limitAnnotation) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        LimitType limitType = limitAnnotation.limitType();
        String name = limitAnnotation.name();
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();
        String key;
        switch (limitType) {
            case IP:
                key = this.getIpAddress();
                break;
            case CUSTOMER:
                key = limitAnnotation.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }

        ImmutableList keys = ImmutableList.of(StringUtils.join(new String[]{limitAnnotation.prefix(), key}));

        try {
            String luaScript = this.buildLuaScript();
            RedisScript<Number> redisScript = new DefaultRedisScript(luaScript, Number.class);
            Number count = (Number) this.limitRedisTemplate.execute(redisScript, keys, new Object[]{limitCount, limitPeriod});
            log.info("Access try count is {} for name={} and key = {}", count, name, key);
            if (count != null && count.intValue() <= limitCount) {
                return pjp.proceed();
            } else {
                throw new RuntimeException("You have been dragged into the blacklist");
            }
        } catch (Throwable var14) {
            if (var14 instanceof RuntimeException) {
                throw new RuntimeException(var14.getLocalizedMessage());
            } else {
                throw new RuntimeException("server exception");
            }
        }
    }

    public String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c");
        lua.append("\nc = redis.call('get',KEYS[1])");
        lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
        lua.append("\nreturn c;");
        lua.append("\nend");
        lua.append("\nc = redis.call('incr',KEYS[1])");
        lua.append("\nif tonumber(c) == 1 then");
        lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
        lua.append("\nend");
        lua.append("\nreturn c;");
        return lua.toString();
    }

    public String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
