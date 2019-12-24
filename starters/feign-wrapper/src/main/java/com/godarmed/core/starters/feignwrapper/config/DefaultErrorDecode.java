package com.godarmed.core.starters.feignwrapper.config;

import com.alibaba.fastjson.JSON;
import com.godarmed.core.starters.feignwrapper.exception.HystrixException;
import com.google.common.base.Strings;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.util.Map;

public class DefaultErrorDecode implements ErrorDecoder {
    public DefaultErrorDecode() {
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        HystrixException exception = null;

        try {
            String json = Util.toString(response.body().asReader());
            if (!Strings.isNullOrEmpty(json)) {
                Map<String, Object> result = (Map) JSON.parseObject(json, Map.class);
                if (result.containsKey("code")) {
                    exception = new HystrixException((Integer)result.get("code"), String.valueOf(result.getOrDefault("message", "未知异常")));
                } else {
                    exception = new HystrixException(response.status(), json);
                }
            } else {
                exception = new HystrixException(response.status(), "未知异常");
            }
        } catch (IOException var6) {
            var6.printStackTrace();
            exception = new HystrixException(response.status(), var6.getMessage());
        }

        return exception;
    }
}
