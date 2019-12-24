package com.godarmed.core.starters.feignwrapper;

import com.godarmed.core.starters.feignwrapper.config.FeignProperties;
import com.godarmed.core.starters.feignwrapper.logger.LoggerRecordService;
import feign.Client;
import feign.Contract;
import feign.Feign.Builder;
import feign.Request.Options;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({FeignClientsConfiguration.class})
@EnableHystrix
@Configuration
@EnableConfigurationProperties({FeignProperties.class})
public class FeignBuilderService {
    @Autowired
    private Builder builder;
    @Autowired
    private Decoder decoder;
    @Autowired
    private Encoder encoder;
    @Autowired
    private Client client;
    @Autowired
    private Contract contract;
    @Autowired
    private FeignProperties config;
    @Autowired
    private Options options;
    @Autowired
    private LoggerRecordService loggerRecordService;

    public FeignBuilderService() {
    }

    public <T> FeignBuilder<T> builder(Class<T> clazz) {
        return (FeignBuilder<T>) this.initBuilder(FeignBuilder.clientBuilder(clazz));
    }

    private FeignBuilder<?> initBuilder(FeignBuilder<?> builder) {
        return builder.addDefault("Builder", this.builder).addDefault("Decoder", this.decoder).addDefault("Encoder", this.encoder).addDefault("Client", this.client).addDefault("Contract", this.contract).addDefault("FeignProperties", this.config).addDefault("Options", this.options).addDefault("LoggerRecordService", this.loggerRecordService);
    }
}
