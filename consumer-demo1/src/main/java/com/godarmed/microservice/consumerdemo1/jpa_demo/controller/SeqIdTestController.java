package com.godarmed.microservice.consumerdemo1.jpa_demo.controller;

import com.eseasky.global.entity.ResultModel;
import com.godarmed.microservice.consumerdemo1.jpa_demo.config.SeqUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

@RestController
@Slf4j
public class SeqIdTestController {

    SeqUtils seqUtils = new SeqUtils("Group_Key");

    @RequestMapping("/getSeqIdTest")
    public ResultModel<List<String>> getSeqIdTest(@NotNull Integer seqIdNum) {
        ResultModel<List<String>> resultModel = new ResultModel<>();
        List<String> seqIdList = new CopyOnWriteArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(seqIdNum);
        for (int i = 0; i < seqIdNum; i++) {
            ((SeqIdTestController) AopContext.currentProxy()).getSeqId(countDownLatch,seqIdList);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resultModel.setData(seqIdList);
        return resultModel;
    }

    @Async("taskExecutor")
    public void getSeqId(CountDownLatch countDownLatch,List<String> seqIdList) {
        String seqId;
        try {
            seqId = seqUtils.getCurrentSeqId();
            seqIdList.add(seqId);
            log.info("线程[{}]获取[{}]",Thread.currentThread().getName(),seqId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage());
        } finally {
            countDownLatch.countDown();
        }
    }
}
