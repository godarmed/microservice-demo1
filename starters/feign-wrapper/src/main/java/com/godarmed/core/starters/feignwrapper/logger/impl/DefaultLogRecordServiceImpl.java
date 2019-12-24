package com.godarmed.core.starters.feignwrapper.logger.impl;

import com.alibaba.fastjson.JSON;
import com.godarmed.core.starters.feignwrapper.logger.LoggerRecordService;
import com.godarmed.core.starters.global.entity.HeaderEntity;
import com.godarmed.core.starters.global.entity.HttpMessage;
import com.godarmed.core.starters.loggerclient.service.LoggerService;
import feign.Request;
import feign.Response;
import feign.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class DefaultLogRecordServiceImpl implements LoggerRecordService {
    @Autowired
    LoggerService loggerService;

    public DefaultLogRecordServiceImpl() {
    }

    @Override
    public Response record(String key, Response response, long elapsedTime) {
        if (response == null) {
            return response;
        } else {
            HttpMessage message = new HttpMessage();
            HeaderEntity headerEntity = null;
            message.setException(response.reason());
            Request request = response.request();

            try {
                if (request != null) {
                    headerEntity = new HeaderEntity(request.headers());
                    message.setUrl(request.url());
                    message.setMethod(request.method());
                    byte[] reqeustBody = request.body();
                    if (reqeustBody != null) {
                        message.setRequestBody(new String(reqeustBody, "utf-8"));
                    }

                    message.setRequestHeader(JSON.toJSONString(request.headers()));
                    message.setRequestId(headerEntity.getRequestId());
                    Response.Body body = response.body();
                    byte[] bodyData = null;
                    if (body != null) {
                        bodyData = Util.toByteArray(response.body().asInputStream());
                        message.setResponseBody(new String(bodyData, "UTF-8"));
                    } else {
                        message.setResponseBody((String)null);
                    }

                    message.setResponseHeader(JSON.toJSONString(response.headers()));
                    message.setResponseTime(Timestamp.from((new Date()).toInstant()));
                    message.setStatus(String.valueOf(response.status()));
                    message.setException(response.reason());
                    message.setElapsedTime(elapsedTime);
                    this.loggerService.logger(message);
                    return response.toBuilder().body(bodyData).build();
                }
            } catch (Exception var11) {
                var11.printStackTrace();
            }

            return response;
        }
    }
}
