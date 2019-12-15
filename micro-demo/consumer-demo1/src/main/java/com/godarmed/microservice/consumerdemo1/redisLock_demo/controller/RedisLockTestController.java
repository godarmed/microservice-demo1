package com.godarmed.microservice.consumerdemo1.redisLock_demo.controller;

import com.godarmed.core.starters.redis.RedisUtils;
import com.godarmed.microservice.consumerdemo1.common.redisLock.annotation.RedisLock;
import lombok.extern.log4j.Log4j2;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Log4j2
@RequestMapping("/redisTest")
@RestController
public class RedisLockTestController {

    private final static String LOCK_KEY = "RedisLockTestController-redislock-test";

    private static Integer commonValue = 0;

    @RequestMapping("/redisLock")
    public void redisLock(Integer threadNum) {
        commonValue = 0;
        for (int i = 0; i < threadNum; i++) {
            ((RedisLockTestController)AopContext.currentProxy()).methodWithLock();
        }
    }

    @RequestMapping("/wrongRedisLock")
    public void wrongRedisLock(Integer threadNum) {
        commonValue = 0;
        for (int i = 0; i < threadNum; i++) {
            ((RedisLockTestController)AopContext.currentProxy()).methodWithWrongLock();
        }
    }

    @RedisLock(lockName = LOCK_KEY,lockTimeout = 1*60*1000)
    @Async
    public void methodWithLockTest(){
        //=======任务内容======
        commonValue++;
        log.info("执行任务[{}],当前值[{}]",Thread.currentThread().getName(),commonValue);
        commonValue--;
        //=======任务内容======
    }

    @Async
    public void methodWithLock(){
        RedisUtils redisUtils = null;
        try{
            redisUtils = new RedisUtils();
            //此操作是互斥的，同一时刻仅有一个持有者
            String requestId = redisUtils.getLock(LOCK_KEY,1*60*1000L,0L);
            if(requestId==null){
                throw new RuntimeException("获取锁失败");
            }

            //=======任务内容======
            commonValue++;
            log.info("执行任务[{}],当前值[{}]",Thread.currentThread().getName(),commonValue);
            Thread.sleep(60*1000L);
            commonValue--;
            //=======任务内容======

            redisUtils.releaseLock(LOCK_KEY,requestId);
        } catch (Exception e) {
            log.info("执行任务[{}]出错,出错原因[{}]",Thread.currentThread().getName(),e.getMessage());
        }finally {
            if(redisUtils!=null){
                redisUtils.close();
            }
        }
    }

    @Async
    public void methodWithWrongLock(){
        RedisUtils redisUtils = null;
        try{
            redisUtils = new RedisUtils();
            //获取redis值的操作不是互斥操作
            String syncDataKey = redisUtils.get(LOCK_KEY);
            //可能有多个线程通过这个判断
            if (syncDataKey != null) {
                throw new RuntimeException("获取锁失败");
            }
            redisUtils.set(LOCK_KEY, 1, 60 * 5);

            //=======任务内容======
            commonValue++;
            log.info("执行任务[{}],当前值[{}]",Thread.currentThread().getName(),commonValue);
            commonValue--;
            //=======任务内容======

            redisUtils.deleteKeys(LOCK_KEY);
        } catch (Exception e) {
            log.info("执行任务[{}]出错,出错原因[{}]",Thread.currentThread().getName(),e.getMessage());
        }finally {
            if(redisUtils!=null){
                redisUtils.close();
            }
        }
    }
}
