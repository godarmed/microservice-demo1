package com.godarmed.microservice.consumerdemo1.jpa_demo.controller;

import com.godarmed.core.starters.global.entity.ResultModel;
import com.godarmed.microservice.consumerdemo1.jpa_demo.config.SeqUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class SeqIdTestController {


    final static String currentSeqIdKey = "Company_Id";

    //创建3个CyclicBarrier对象,执行完后执行当前类的run方法
    private CyclicBarrier cb = new CyclicBarrier(3);

    @RequestMapping("/getSeqIdTest")
    public ResultModel<List<String>> getSeqIdTest(@NotNull Integer seqIdNum) {
        ResultModel<List<String>> resultModel = new ResultModel<>();
        List<String> seqIdList = new CopyOnWriteArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(seqIdNum);
        cb = new CyclicBarrier(seqIdNum);
        for (int i = 0; i < seqIdNum; i++) {
            ((SeqIdTestController) AopContext.currentProxy()).getSeqId(countDownLatch, seqIdList);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //检测有无重复数据
        List<String> distinctList = seqIdList.stream().distinct().collect(Collectors.toList());
        if (distinctList.size() < seqIdList.size()) {
            log.info("存在重复数据[{}]个", seqIdList.size() - distinctList.size());
        }
        resultModel.setData(seqIdList);
        return resultModel;
    }

    @Async("taskExecutor")
    public void getSeqId(CountDownLatch countDownLatch, List<String> seqIdList) {
        String seqId;
        try {
            this.cb.await(10L, TimeUnit.SECONDS);
            Thread.sleep(1);
            seqId = SeqUtils.getCurrentSeqId(currentSeqIdKey);
            seqIdList.add(seqId);
            log.info("线程[{}]获取[{}]", Thread.currentThread().getName(), seqId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            countDownLatch.countDown();
        }
    }

    public static void main(String[] args) {
        List<String> seqIdList = Arrays.asList("a", "b", "c", "a", "c");
        List<String> distinctList = seqIdList.stream().distinct().collect(Collectors.toList());
        if (distinctList.size() < seqIdList.size()) {
            log.info("存在重复数据[{}]个", seqIdList.size() - distinctList.size());
        }
    }
}
