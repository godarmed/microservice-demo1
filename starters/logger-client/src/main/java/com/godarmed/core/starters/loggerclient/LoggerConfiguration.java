package com.godarmed.core.starters.loggerclient;

import com.alibaba.fastjson.JSON;
import com.godarmed.core.starters.global.entity.HeaderEntity;
import com.godarmed.core.starters.global.entity.HttpMessage;
import com.godarmed.core.starters.loggerclient.annotation.RequestStart;
import com.godarmed.core.starters.loggerclient.service.LoggerService;
import com.godarmed.core.starters.loggerclient.service.impl.MqLoggerServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Aspect
@Configuration
@EnableConfigurationProperties({LoggerProperties.class})
public class LoggerConfiguration {

    @Autowired
    LoggerProperties loggerProperties;

    public LoggerConfiguration() {
    }

    @Bean
    public LoggerService getLoggerService() {
        return new MqLoggerServiceImpl();
    }

    @Pointcut("@annotation(com.godarmed.core.starters.loggerclient.annotation.RequestStart)")
    public void serviceAspect() {
    }

    @Around("serviceAspect() && @annotation(annotation)")
    public Object Interceptor(ProceedingJoinPoint proceedingJoinPoint, RequestStart annotation) throws Throwable {
        Object response = null;
        HttpMessage message = null;
        long start = 0L;

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            message = new HttpMessage();
            Map<String, Collection<String>> headerMap = this.getHeaders(request);
            HeaderEntity headerEntity = new HeaderEntity(headerMap);
            message.setUrl(request.getRequestURI());
            message.setMethod(request.getMethod());
            message.setRequestBody(JSON.toJSONString(proceedingJoinPoint.getArgs()));
            message.setRequestHeader(JSON.toJSONString(headerMap));
            message.setRequestId(headerEntity.getRequestId());
            start = System.currentTimeMillis();
            response = proceedingJoinPoint.proceed();
            message.setResponseHeader((String) null);
            message.setResponseTime(Timestamp.from((new Date()).toInstant()));
            message.setStatus("200");
            message.setException((String) null);
            message.setElapsedTime(System.currentTimeMillis() - start);
            if (response instanceof MultipartFile) {
                Map<String, Object> fileJson = new HashMap<>();
                fileJson.put("fileName", ((MultipartFile) response).getOriginalFilename());
                fileJson.put("fileSize", ((MultipartFile) response).getSize());
                message.setResponseBody(JSON.toJSONString(fileJson));
            } else {
                message.setResponseBody(JSON.toJSONString(response));
            }

            this.getLoggerService().logger(message);
        } catch (Throwable var11) {
            var11.printStackTrace();
            if (message != null) {
                message.setResponseHeader((String) null);
                message.setResponseTime(Timestamp.from((new Date()).toInstant()));
                message.setStatus("200");
                message.setException(var11.getLocalizedMessage());
                message.setElapsedTime(System.currentTimeMillis() - start);
                message.setResponseBody((String) null);
                this.getLoggerService().logger(message);
                throw var11;
            }
        }

        return response;
    }

    private Map<String, Collection<String>> getHeaders(HttpServletRequest request) {
        Enumeration<String> names = request.getHeaderNames();
        Map<String, Collection<String>> headers = new HashMap<>();
        String headerName = null;

        while (names.hasMoreElements()) {
            headerName = (String) names.nextElement();
            headers.put(headerName, Arrays.asList(request.getHeader(headerName)));
        }

        return headers;
    }
}
