package com.godarmed.core.starters.feignwrapper.logger;

import feign.Logger;
import feign.Request;
import feign.Response;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Slf4jRecordLogger extends Logger {
    private final org.slf4j.Logger logger;
    private final LoggerRecordService loggerRecordService;

    public Slf4jRecordLogger() {
        this(Logger.class);
    }

    public Slf4jRecordLogger(Class<?> clazz) {
        this((org.slf4j.Logger) LoggerFactory.getLogger(clazz), (LoggerRecordService)null);
    }

    public Slf4jRecordLogger(Class<?> clazz, LoggerRecordService loggerRecordService) {
        this(LoggerFactory.getLogger(clazz), loggerRecordService);
    }

    public Slf4jRecordLogger(String name) {
        this((org.slf4j.Logger)LoggerFactory.getLogger(name), (LoggerRecordService)null);
    }

    Slf4jRecordLogger(org.slf4j.Logger logger, LoggerRecordService loggerRecordService) {
        this.logger = logger;
        this.loggerRecordService = loggerRecordService;
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (this.logger.isDebugEnabled()) {
            super.logRequest(configKey, logLevel, request);
        }

    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        Response responseReturn = response;
        if (this.loggerRecordService != null) {
            responseReturn = this.loggerRecordService.record(configKey, response, elapsedTime);
        }

        return this.logger.isDebugEnabled() ? super.logAndRebufferResponse(configKey, logLevel, responseReturn, elapsedTime) : responseReturn;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format(methodTag(configKey) + format, args));
        }

    }
}
