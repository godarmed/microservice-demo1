package com.godarmed.microservice.consumerdemo1.jpa_demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
public class ThreadPoolConfiguration implements AsyncConfigurer{
    public static void main(String[] args) {
        String source = "{\"fileId\":\"a2a123645561432583ec184aafc90261\"}";
        System.out.println(source.substring(source.indexOf(":")+2,source.lastIndexOf("}")-1));
    }
    /**
     * 核心线程数：线程池创建时候初始化的线程数
     *//*
    @Value("${executor.core-pool-size}")
    private int corePoolSize;

    *//**
     * 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
     *//*
    @Value("${executor.max-pool-size}")
    private int maxPoolSize;

    *//**
     * 缓冲队列200：用来缓冲执行任务的队列
     *//*
    @Value("${executor-queue-capacity}")
    private int queueCapacity;

    *//**
     * 允许线程的空闲时间(单位：秒)：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
     *//*
    @Value("${executor.keepalive-Seconds}")
    private int keepAliveSeconds;

    */
    /**
     * 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
     *//*
    @Value("${executor.thread-name-prefix}")
    private String threadNamePrefix;*/

    //线程池配置
    @Bean("taskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(15);
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setQueueCapacity(2000);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setAllowCoreThreadTimeOut(true);
        taskExecutor.setThreadNamePrefix("线程池L-");
        taskExecutor.setTaskDecorator(new ContextDecorator());
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    //异常处理配置（对于无返回值的方法）
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SpringAsyncExceptionHandler();
    }

    //自定义异常处理类
    class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            log.error("Exception occurs in async method", throwable.getMessage());
        }
    }

    //线程上下文传递配置
    class ContextDecorator implements TaskDecorator{
        @Override
        public Runnable decorate(Runnable runnable) {
            RequestAttributes context = RequestContextHolder.currentRequestAttributes();
            return () -> {
                try{
                    //执行异步任务前添加上下文
                    RequestContextHolder.setRequestAttributes(context,true);
                    //执行异步任务
                    runnable.run();
                }finally{
                    //执行异步任务后重置上下文
                    RequestContextHolder.resetRequestAttributes();
                }
            };
        }
    }

    /*@Bean
    public Executor MessageExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(threadNamePrefix);

        // 线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }*/

}
