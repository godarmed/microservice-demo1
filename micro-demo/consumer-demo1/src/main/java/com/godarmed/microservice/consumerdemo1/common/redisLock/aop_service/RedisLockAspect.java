package com.godarmed.microservice.consumerdemo1.common.redisLock.aop_service;

import com.alibaba.fastjson.JSON;
import com.godarmed.core.starters.global.entity.HeaderEntity;
import com.godarmed.core.starters.global.entity.HttpMessage;
import com.godarmed.core.starters.loggerclient.annotation.RequestStart;
import com.godarmed.core.starters.redis.RedisUtils;
import com.godarmed.microservice.consumerdemo1.common.redisLock.annotation.RedisLock;
import com.godarmed.microservice.consumerdemo1.common.timeLog.aop_service.LogAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component("RedisLockAspect")
public class RedisLockAspect {
    private final Logger log = LoggerFactory.getLogger(LogAspect.class);

    private static final ThreadLocal<String> LOCK_NAME = new NamedThreadLocal<String>("lock name");

    private static final ThreadLocal<Long> LOCK_TIMEOUT = new NamedThreadLocal<Long>("lock timeout");

    private static final ThreadLocal<String> CLASS_NAME = new NamedThreadLocal<String>("class name");

    private static final ThreadLocal<String> METHOD_NAME = new NamedThreadLocal<String>("method name");

    private static final ThreadLocal<String> ARGS = new NamedThreadLocal<String>("method ARGS ");

    //织入点
    @Pointcut("@within(com.godarmed.microservice.consumerdemo1.common.redisLock.annotation.RedisLock)" +
            "||@annotation(com.godarmed.microservice.consumerdemo1.common.redisLock.annotation.RedisLock)")
    public void redisPointCut() {
    }

    @Around("redisPointCut() && @annotation(annotation)")
    public Object Interceptor(ProceedingJoinPoint proceedingJoinPoint, RedisLock annotation) throws Throwable {
        doBeforeMethod(proceedingJoinPoint);
        Object response = null;
        RedisUtils redisUtils = null;
        try {
            redisUtils = new RedisUtils();
            //获取锁
            String requestId = redisUtils.getLock(LOCK_NAME.get(),LOCK_TIMEOUT.get(),0L);
            if(requestId==null){
                throw new RuntimeException(METHOD_NAME+"获取锁失败");
            }
            response = proceedingJoinPoint.proceed();
            //释放锁
            redisUtils.releaseLock(LOCK_NAME.get(),requestId);
        } catch (Throwable e) {
            log.info(e.getMessage());
        }finally {
            doAfterMethod();
            if(redisUtils!=null){
                redisUtils.close();
            }
        }
        return response;
    }

    /**
     * 是否存在某个注解，如果存在就获取
     */
    private static <T extends Annotation> T getAnnotationLog(JoinPoint joinPoint, Class<T> annotationClass) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(annotationClass);
        }
        return null;
    }

    /**
     * 前置初始化方法
     */
    private void doBeforeMethod(JoinPoint joinPoint) {
        try {
            //获得注解中的属性,设置锁的名称和入参
            Signature signature = joinPoint.getSignature();
            RedisLock annotation = ((MethodSignature) signature).getMethod().getAnnotation(RedisLock.class);
            if(annotation!=null){
                LOCK_NAME.set(annotation.lockName());
                LOCK_TIMEOUT.set(annotation.lockTimeout());
            }

            // 获得类名称
            String className = getRealClass(joinPoint);
            // 获得方法名称
            String methodName = joinPoint.getSignature().getName();
            //获得入参
            String args = JSON.toJSONString(joinPoint.getArgs());

            //打印日志，如有需要还可以存入数据库
            CLASS_NAME.set(className);
            METHOD_NAME.set(methodName);
            ARGS.set(args);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 后置重置方法（防止线程复用造成错误）
     */
    private void doAfterMethod() {
        try {
            //清空所有ThreadLocal
            LOCK_NAME.remove();
            LOCK_TIMEOUT.remove();
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
    private String getRealClass(JoinPoint joinPoint) {
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
