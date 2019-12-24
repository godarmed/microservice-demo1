package com.godarmed.core.starters.feignwrapper.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class DefaultHeaderSet implements HeaderSet {
    private static final Logger log = LogManager.getLogger(DefaultHeaderSet.class);

    public DefaultHeaderSet() {
    }

    @Override
    public Map<String, Object> build() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            Enumeration<String> names = request.getHeaderNames();
            String key = null;
            HashMap params = new HashMap();

            while(names.hasMoreElements()) {
                key = (String)names.nextElement();
                params.put(key, request.getHeader(key));
            }

            return params;
        } catch (Exception var5) {
            log.warn(var5.getCause());
            return new HashMap();
        }
    }
}
