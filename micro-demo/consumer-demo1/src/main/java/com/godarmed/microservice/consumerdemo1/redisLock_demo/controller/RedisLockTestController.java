package com.godarmed.microservice.consumerdemo1.redisLock_demo.controller;

import com.godarmed.core.starters.redis.RedisUtils;
import com.godarmed.core.starters.redis.lock.RLockHandler;
import com.godarmed.core.starters.redis.lock.annotation.RedisLock;
import com.godarmed.core.starters.redis.lock.annotation.RedisLockName;
import com.godarmed.core.starters.redis.lock.annotation.RedisLockTimeOut;
import com.godarmed.core.starters.redis.lock.annotation.RedisLockWaitTime;
import com.godarmed.microservice.consumerdemo1.common.protocol.vo.RequestMsg;
import lombok.extern.log4j.Log4j2;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            String lockName = new String(LOCK_KEY);
            ((RedisLockTestController)AopContext.currentProxy()).methodWithRLock(lockName);
        }
    }

    @RequestMapping("/wrongRedisLock")
    public void wrongRedisLock(Integer threadNum) {
        commonValue = 0;
        for (int i = 0; i < threadNum; i++) {
            ((RedisLockTestController)AopContext.currentProxy()).methodWithWrongLock();
        }
    }

    @Async
    public void methodWithRLock(String lockName){
        RLock rLock = null;
        try{
            //此操作是互斥的，同一时刻仅有一个持有者
            if((rLock = RLockHandler.lock(lockName,1*60))==null){
                throw new RuntimeException("获取锁失败");
            }

            //=======任务内容======
            commonValue++;
            log.info("执行任务[{}],当前值[{}]",Thread.currentThread().getName(),commonValue);
            //Thread.sleep(3000L);
            commonValue--;
            //=======任务内容======


        } catch (Exception e) {
            log.info("执行任务[{}]出错,出错原因[{}]",Thread.currentThread().getName(),e.getMessage());
        }finally {
            try{
                RLockHandler.release(rLock);
            }catch (Exception e){
                log.info(e.getMessage());
            }

        }
    }

    @RedisLock(lockName = LOCK_KEY,lockTimeout = 1*60*1000,waitTimeout = 0L)
    @Async
    public void methodWithLockTest(@RedisLockName String lockName, @RedisLockTimeOut Long lockTime, @RedisLockWaitTime Long waitTime){
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
            //Thread.sleep(60*1000L);
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
