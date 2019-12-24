package com.godarmed.core.starters.feignwrapper;

import com.alibaba.fastjson.JSON;
import com.godarmed.core.starters.feignwrapper.exception.HystrixException;
import com.godarmed.core.starters.global.entity.ResultModel;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@Configuration
@ControllerAdvice
public class FeignExceptionHandler {
    private static final Logger log = LogManager.getLogger(FeignExceptionHandler.class);

    public FeignExceptionHandler() {
    }

    @ExceptionHandler({HystrixRuntimeException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultModel<String> otherExceptionHandler(HystrixRuntimeException e) {
        ResultModel<String> error = new ResultModel();
        error.setSubCode(500);
        error.setMessage(e.getMessage());
        Throwable cause = e.getCause();
        if (e.getFallbackException() != null && e.getFallbackException().getCause() != null && e.getFallbackException().getCause().getCause().getClass().isAssignableFrom(HystrixException.class)) {
            HystrixException hystrixException = (HystrixException)e.getFallbackException().getCause().getCause();
            error.setSubCode(hystrixException.getCode());
            error.setMessage(hystrixException.getMsg());
        }

        if (cause != null && cause.getClass().isAssignableFrom(FeignException.class)) {
            FeignException feignException = (FeignException)cause;
            String message = feignException.getMessage();
            log.error(message);
            if (message.indexOf("{") >= 0 && message.indexOf("}") > 0) {
                message = message.substring(message.indexOf("{"), message.indexOf("}") + 1);
                Map<String, Object> res = (Map) JSON.parseObject(message, Map.class);
                if (res.containsKey("code")) {
                    error.setSubCode((Integer)res.getOrDefault("code", 500));
                    error.setMessage((String)res.getOrDefault("message", feignException.getMessage()));
                }
            }
        }

        return error;
    }
}

