package com.godarmed.core.starters.feignwrapper.interceptor;

import com.godarmed.core.starters.feignwrapper.config.FeignProperties;
import com.godarmed.core.starters.feignwrapper.config.HeaderSet;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class HeaderCopyInterceptor implements RequestInterceptor {
    private static final Logger log = LogManager.getLogger(HeaderCopyInterceptor.class);
    private FeignProperties config;
    private List<String> inheritHeaders;

    public HeaderCopyInterceptor(FeignProperties config) {
        this(config, (List)null);
    }

    public HeaderCopyInterceptor(FeignProperties config, List<String> inheritHeaders) {
        this.config = null;
        this.inheritHeaders = new ArrayList<String>() {
            private static final long serialVersionUID = 1L;

            {
                this.add("authorization");
            }
        };
        this.config = config;
        if (inheritHeaders != null && inheritHeaders.size() > 0) {
            this.inheritHeaders.addAll(inheritHeaders);
        }

    }

    @Override
    public void apply(RequestTemplate template) {
        log.info("feign=====>feign reqeust for {}", template.url());
        Map<String, Object> headers = this.getHeader(this.config);
        if (!headers.containsKey("requestId")) {
            template.header("requestId", new String[]{UUID.randomUUID().toString()});
        }

        if (headers != null && !headers.isEmpty()) {
            Iterator<String> keys = headers.keySet().iterator();
            Map<String, Collection<String>> currentHeaders = template.headers();
            String key = null;

            label37:
            while(true) {
                do {
                    do {
                        do {
                            if (!keys.hasNext()) {
                                break label37;
                            }

                            key = (String)keys.next();
                        } while(currentHeaders.containsKey(key));
                    } while(!this.inheritHeaders.contains(key.toLowerCase()));
                } while(template.url().equals("/dict/queryByTypeAndGroup") && key.toLowerCase().equals("authorization") && String.valueOf(headers.get(key)).toLowerCase().startsWith("basic"));

                template.header(key, new String[]{String.valueOf(headers.get(key))});
            }
        }

        log.info("feign=====>feign reqeust header for {}", template.headers());
    }

    private Map<String, Object> getHeader(FeignProperties config) {
        if (config.getHeaderClass() != null) {
            try {
                Class<? extends HeaderSet> headerSet = (Class<? extends HeaderSet>) Class.forName(config.getHeaderClass());
                return ((HeaderSet)headerSet.newInstance()).build();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public void setConfig(FeignProperties config) {
        this.config = config;
    }

    public FeignProperties getConfig() {
        return this.config;
    }
}
