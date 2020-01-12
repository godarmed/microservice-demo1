package com.godarmed.core.starters.redis.lock;

import com.godarmed.core.starters.redis.RedisUtils;
import com.godarmed.core.starters.redis.lock.annotation.RedisLock;
import com.godarmed.core.starters.redis.lock.annotation.RedisLockName;
import com.godarmed.core.starters.redis.lock.annotation.RedisLockTimeOut;
import com.godarmed.core.starters.redis.lock.annotation.RedisLockWaitTime;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

@Aspect
@Component("RedisLockAspect")
public class RedisLockInterceptor {
    private final Logger log = LoggerFactory.getLogger(RedisLockInterceptor.class);

    private static final ThreadLocal<String> LOCK_NAME = new NamedThreadLocal<String>("lock name");

    private static final ThreadLocal<Long> LOCK_TIMEOUT = new NamedThreadLocal<Long>("lock timeout");

    private static final ThreadLocal<Long> WAIT_TIMEOUT = new NamedThreadLocal<Long>("wait timeout");

    private static final ThreadLocal<String> CLASS_NAME = new NamedThreadLocal<String>("class name");

    private static final ThreadLocal<String> METHOD_NAME = new NamedThreadLocal<String>("method name");

    private static final ThreadLocal<Object[]> ARGS = new NamedThreadLocal<Object[]>("method ARGS ");

    //织入点
    @Pointcut("@annotation(com.godarmed.core.starters.redis.lock.annotation.RedisLock)")
    public void redisPointCut() {
    }

    @Around("redisPointCut()")
    public Object Interceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //设置参数
        initiateArgs(proceedingJoinPoint);
        Object response = null;
        RedisUtils redisUtils = null;
        String requestId = null;
        try {
            redisUtils = new RedisUtils();
            //获取锁
            requestId = redisUtils.getLock(LOCK_NAME.get(), LOCK_TIMEOUT.get(), 0L);
            if (requestId == null) {
                throw new RuntimeException(METHOD_NAME.get() + "获取锁失败");
            }
            response = proceedingJoinPoint.proceed();

        } catch (Throwable e) {
            log.info(e.getMessage());
        } finally {
            //释放锁
            redisUtils.releaseLock(LOCK_NAME.get(), requestId);
            //释放工具类
            if (redisUtils != null) {
                redisUtils.close();
            }
            //清除ThreadLocal中的变量
            removeArgs();
        }
        return response;
    }

    /**
     * 是否存在某个注解，如果存在就获取
     */
    private final <T extends Annotation> T getAnnotationLog(JoinPoint joinPoint, Class<T> annotationClass) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(annotationClass);
        }
        return null;
    }

    /**
     * 初始化公共参数
     */
    private final void initiateCommonArgs(JoinPoint joinPoint) {
        // 获得类名称
        String className = getRealClass(joinPoint);
        // 获得方法名称
        String methodName = joinPoint.getSignature().getName();
        //获得入参
        Object[] args = joinPoint.getArgs();

        //打印日志，如有需要还可以存入数据库
        CLASS_NAME.set(className);
        METHOD_NAME.set(methodName);
        ARGS.set(args);

        if(log.isDebugEnabled()){
            log.debug("当前类名称为[{}]",CLASS_NAME.get());
            log.debug("当前方法为[{}]",METHOD_NAME.get());
            log.debug("方法参数为[{}]",ARGS.get());
        }
    }

    /**
     * 初始化lock参数
     */
    private final void initiateLockArgs(JoinPoint joinPoint) {
        //获得注解中的属性,设置锁的名称和入参
        Signature signature = joinPoint.getSignature();
        Method method = ((MethodSignature) signature).getMethod();

        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        if (redisLock != null) {
            LOCK_NAME.set(redisLock.lockName());
            LOCK_TIMEOUT.set(redisLock.lockTimeout());
            WAIT_TIMEOUT.set(redisLock.waitTimeout());
        }
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();
        if (parameterAnnotations != null && parameterAnnotations.length > 0) {
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation instanceof RedisLockName && args[i] != null && args[i] instanceof String ) {
                        String lockName = (String)args[i];
                        if(Strings.isNotBlank(lockName)){
                            LOCK_NAME.set((String)args[i]);
                        }
                    }
                    if (annotation instanceof RedisLockTimeOut && args[i] != null && args[i] instanceof Long) {
                        Long lockTimeOut = (Long)args[i];
                        LOCK_TIMEOUT.set(lockTimeOut);
                    }
                    if (annotation instanceof RedisLockWaitTime && args[i] != null && args[i] instanceof Long) {
                        Long waitTimeOut = (Long)args[i];
                        WAIT_TIMEOUT.set(waitTimeOut);
                    }
                }
            }
        }
        if(log.isDebugEnabled()){
            log.debug("获取的锁名称为[{}]",LOCK_NAME.get());
            log.debug("获取的锁的过期时间为[{}]",LOCK_TIMEOUT.get());
            log.debug("获取锁的等待时间为[{}]",WAIT_TIMEOUT.get());
        }
    }

    /**
     * 初始化方法
     */
    private final void initiateArgs(JoinPoint joinPoint) {
        //初始化公共参数
        initiateCommonArgs(joinPoint);
        //获得注解中的属性,设置锁的名称和入参
        initiateLockArgs(joinPoint);

    }

    /**
     * 后置重置方法（防止线程复用造成错误）
     */
    private final void removeArgs() {
        try {
            //清空所有ThreadLocal
            LOCK_NAME.remove();
            LOCK_TIMEOUT.remove();
            WAIT_TIMEOUT.remove();
            CLASS_NAME.remove();
            METHOD_NAME.remove();
            ARGS.remove();
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==后置通知异常==异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取方法的实际类
     * PS:因为直接获取的是代理类，需要获取实际类型的
     */
    private final String getRealClass(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        //如果是代理类
        if (className.contains("com.sun.proxy.$Proxy")) {
            //查看有无继承的父类
            Type[] interfaces = joinPoint.getTarget().getClass().getGenericInterfaces();
            if (interfaces.length > 0) {
                className = interfaces[0].getTypeName();
            } else {
                //查看有无实现的接口
                Type superClass = joinPoint.getTarget().getClass().getGenericSuperclass();
                if (superClass != null) {
                    className = superClass.getTypeName();
                }
            }
        }
        return className;
    }
}
