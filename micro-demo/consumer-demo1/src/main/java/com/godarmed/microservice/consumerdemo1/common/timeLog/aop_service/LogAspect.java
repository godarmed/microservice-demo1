package com.godarmed.microservice.consumerdemo1.common.timeLog.aop_service;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;

@Aspect
@Component("LogAspect")
public class LogAspect {
    private final Logger log = LoggerFactory.getLogger(LogAspect.class);

    private static final ThreadLocal<Long> beginTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal beginTime");

    private static final ThreadLocal<Long> endTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal endTime");

    private static final ThreadLocal<String> className = new NamedThreadLocal<String>("class name");

    private static final ThreadLocal<String> methodName = new NamedThreadLocal<String>("method name");

    private static final ThreadLocal<String> args = new NamedThreadLocal<String>("method args");

    //织入点
    @Pointcut("@within(com.godarmed.microservice.consumerdemo1.common.timeLog.annotation.Log)" +
            "||@annotation(com.godarmed.microservice.consumerdemo1.common.timeLog.annotation.Log)")
    public void logPointCut(){}

    /**
     * 前置通知 用于拦截操作,在方法返回后执行
     */
    @Before(value = "logPointCut()")
    public void doBefore(JoinPoint joinPoint){
        doBeforeMethod(joinPoint);
        //获取启动时间,当前类和当前方法
        log.info("[前置通知][{}]类中的[{}]方法启动,\n其入参为[{}],开始时间：[{}]",className.get(),methodName.get(),args.get(),new Date(beginTimeThreadLocal.get()));
    }

    /**
     * 后置通知 用于拦截操作,在方法返回后执行（先于返回通知或者返回异常通知）
     */
    @After(value = "logPointCut()")
    public void doAfter(JoinPoint joinPoint){
        endTimeThreadLocal.set(System.currentTimeMillis());
        log.info("[后置通知]:[{}]类中的[{}]方法结束,方法结束时间[{}],耗时[{}]毫秒",className.get(),methodName.get(),new Date(endTimeThreadLocal.get()),endTimeThreadLocal.get()-beginTimeThreadLocal.get());
    }

    /**
     * 返回通知 用于拦截操作,在方法返回后执行
     */
    @SuppressWarnings("unchecked")
    @AfterReturning(value = "logPointCut()",argNames = "joinPoint,result",returning = "result")
    public void doAfterReturn(JoinPoint joinPoint, Object result){
        Object[] object = joinPoint.getArgs();
        log.info("[返回通知]:[{}]类中的[{}]方法的返回为[{}]",className.get(),methodName.get(), JSON.toJSONString(result));
        doAfterMethod();
    }

    /**
     * 后置异常通知 用于拦截操作,在方法出现异常后执行
     */
    @AfterThrowing(value = "logPointCut()",throwing ="e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e){
        log.info("[后置异常通知]:[{}]类中的[{}]方法产生异常[{}]",className.get(),methodName.get(),e.getCause());
        doAfterMethod();
    }

    /**
     * 是否存在某个注解，如果存在就获取
     */
    private static <T extends Annotation> T getAnnotationLog(JoinPoint joinPoint, Class<T> annotationClass){
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
            // 获得类名称
            String className = getRealClass(joinPoint);
            // 获得方法名称
            String methodName = joinPoint.getSignature().getName();
            //获得入参
            String args = JSON.toJSONString(joinPoint.getArgs());

            //打印日志，如有需要还可以存入数据库
            LogAspect.className.set(className);
            LogAspect.methodName.set(methodName);
            LogAspect.args.set(args);
            LogAspect.beginTimeThreadLocal.set(System.currentTimeMillis());
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
            LogAspect.className.remove();
            LogAspect.methodName.remove();
            LogAspect.beginTimeThreadLocal.remove();
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
    private String getRealClass(JoinPoint joinPoint){
        String className = joinPoint.getTarget().getClass().getName();
        if(className.indexOf("com.sun.proxy.$Proxy") !=  -1){   //如果是代理类
            //查看有无继承的父类
            Type[] interfaces = joinPoint.getTarget().getClass().getGenericInterfaces();
            if(interfaces.length > 0){
                className = interfaces[0].getTypeName();
            }else{
                //查看有无实现的接口
                Type superClass = joinPoint.getTarget().getClass().getGenericSuperclass();
                if(superClass != null){
                    className = superClass.getTypeName();
                }
            }
        }
        return className;
    }
}
