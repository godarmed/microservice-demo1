package com.godarmed.microservice.protocol.feigntestprotocol;

import com.godarmed.core.starters.feignwrapper.FeignClientFactory;
import com.godarmed.microservice.protocol.feigntestprotocol.protocol.FeignTestServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnBean(FeignClientFactory.class)
@Configuration
public class FeignTestConfig {

    @Autowired
    FeignClientFactory factory;

    @Bean
    public FeignTestServiceFeign pictureServiceFeign() {
        return factory.clientFeignContract(FeignTestServiceFeign.class);
    }

    /*@Bean
    public FeignTestServiceFeign pictureServiceFeign() {
        return factory.multipartclientFeign(FeignTestServiceFeign.class,new MultipartEncoder());
    }*/
}
