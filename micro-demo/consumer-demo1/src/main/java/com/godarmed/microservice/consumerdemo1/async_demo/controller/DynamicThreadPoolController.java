package com.godarmed.microservice.consumerdemo1.async_demo.controller;

import com.godarmed.core.starters.global.utils.SpringUtils;
import com.godarmed.microservice.consumerdemo1.common.config.ThreadPoolConfiguration;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@RequestMapping("/AsyncTest")
@RestController
public class DynamicThreadPoolController {

    @Autowired
    ThreadPoolConfiguration executorConfig;

    @RequestMapping("/changeCorePoolSize")
    public void changeCorePoolSize(Integer corePoolSize) {
        executorConfig.changeCorePoolSize(corePoolSize);
    }

    @RequestMapping("/changeMaxPoolSize")
    public void changeMaxPoolSize(Integer maxPoolSize) {
        executorConfig.changeMaxPoolSize(maxPoolSize);
    }

    @RequestMapping("/DynPool")
    public void asyncTest(Integer num) {
        DynamicThreadPoolController controller = SpringUtils.getBean(DynamicThreadPoolController.class);
        for (int i = 0; i < num; i++) {
            controller.executeAsync();
        }
    }

    ThreadLocalRandom random = ThreadLocalRandom.current();

    @Async("taskExecutor")
    public void executeAsync() {
        log.info("start executeAsync");
        try {
            long randomLong = random.nextLong(0, 5000);
            Thread.sleep(randomLong + 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("end executeAsync");
    }
}
