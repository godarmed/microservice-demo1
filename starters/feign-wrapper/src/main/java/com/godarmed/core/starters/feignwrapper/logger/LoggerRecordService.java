package com.godarmed.core.starters.feignwrapper.logger;

import feign.Response;

public interface LoggerRecordService {
    Response record(String var1, Response var2, long var3);
}
