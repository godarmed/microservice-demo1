package com.godarmed.microservice.protocol.feigntestprotocol;

import com.godarmed.core.starters.feignwrapper.FeignClientFactory;
import com.godarmed.core.starters.feignwrapper.config.multipart.DefaultMultipartFileEncoder;
import com.godarmed.core.starters.feignwrapper.config.multipart.MultipartEncoder;
import com.godarmed.microservice.protocol.feigntestprotocol.protocol.FeignTestServiceFeign;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnBean(FeignClientFactory.class)
@Configuration
public class FeignTestConfig {

    @Autowired
    FeignClientFactory factory;

    @Autowired
    ObjectFactory<HttpMessageConverters> messageConverters;

    /*@Bean
    public FeignTestServiceFeign pictureServiceFeign() {
        return factory.clientFeignContract(FeignTestServiceFeign.class);
    }*/

    @Bean
    public FeignTestServiceFeign pictureServiceFeign() {
        return factory.multipartclientFeign(FeignTestServiceFeign.class,new DefaultMultipartFileEncoder(new SpringEncoder(messageConverters)));
    }
}
