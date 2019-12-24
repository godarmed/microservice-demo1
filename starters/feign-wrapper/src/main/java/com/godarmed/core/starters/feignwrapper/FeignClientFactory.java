package com.godarmed.core.starters.feignwrapper;

import com.godarmed.core.starters.feignwrapper.fallbacks.DefaultFallback;
import feign.Contract;
import feign.Feign;
import feign.codec.Encoder;
import feign.hystrix.FallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FeignClientFactory {
    @Autowired
    private FeignBuilderService feignBuilderService;

    public FeignClientFactory() {
    }

    public <T> T client(Class<T> feignClient) {
        return this.feignBuilderService.builder(feignClient).toDefaultClient();
    }

    public <T> T client(Class<T> feignClient, List<String> inheritHeaders) {
        return this.feignBuilderService.builder(feignClient).addCopyHeaders(inheritHeaders).toDefaultClient();
    }

    public <T> T clientFeignContract(Class<T> feignClient) {
        return this.clientFeignContract(feignClient, (List<String>) null);
    }

    public <T> T clientFeignContract(Class<T> feignClient, List<String> inheritHeaders) {
        T res = this.feignBuilderService.builder(feignClient).contract(new Contract.Default()).addCopyHeaders(inheritHeaders).toDefaultClient();
        return res;
    }

    public <T> T multipartclientFeign(Class<T> feignClient, Encoder multipartEncoder) {
        return this.multipartClientFeign(feignClient, multipartEncoder, (List<String>) null);
    }

    public <T> T multipartClientFeign(Class<T> feignClient, Encoder multipartEncoder, List<String> inheritHeaders) {
        return this.feignBuilderService.builder(feignClient).contract(new Contract.Default()).encoder(multipartEncoder).addCopyHeaders(inheritHeaders).toDefaultClient();
    }

    public <T> T clientWithFallback(Feign.Builder builder, Class<T> feignClient, String serviceName, Map<String, Object> headers, FallbackFactory<T> fallback) {
        return this.feignBuilderService.builder(feignClient).builder(builder).addStaticHeaders(headers).fallbackFactory(fallback).toDefaultClient(serviceName);
    }

    public <T> T clientWithFallback(Class<T> feignClient, String serviceName, Map<String, Object> headers) {
        return this.feignBuilderService.builder(feignClient).addStaticHeaders(headers).toDefaultClient(serviceName);
    }

    public <T> T clientWithFallback(Feign.Builder builder, Class<T> feignClient, String serviceName, Map<String, Object> headers) {
        return this.clientWithFallback(builder, feignClient, serviceName, headers, (FallbackFactory<T>) new DefaultFallback(feignClient));
    }
}
