package com.godarmed.core.starters.feignwrapper.targets;

import com.godarmed.core.starters.feignwrapper.config.Feign;
import com.godarmed.core.starters.feignwrapper.config.FeignAnnotationHandler;
import com.godarmed.core.starters.feignwrapper.config.FeignProperties;
import feign.Request;
import feign.RequestTemplate;
import feign.Target;
import lombok.Data;

import java.util.Iterator;
import java.util.Map;

@Data
public class DefaultDynamicTarget<T> implements Target<T> {
    private Class<T> type = null;
    private String serviceName = null;
    private String url = null;
    private Map<String, Object> headers = null;
    private FeignProperties config = null;

    public DefaultDynamicTarget(Class<T> type, FeignProperties config) {
        Feign params = FeignAnnotationHandler.handler(type);
        this.setType(type);
        if (params != null) {
            this.setServiceName(params.serviceName());
        }

        if (!"".equals(this.getServiceName()) || this.getServiceName() == null) {
            this.setUrl(config.getProtocol() + "://" + config.getGateway() + "/" + this.serviceName);
        }

    }

    public DefaultDynamicTarget(Class<T> type, String serviceName, Map<String, Object> headers) {
        this.setType(type);
        this.setServiceName(serviceName);
        this.setUrl("http://Gateway/" + serviceName);
        this.setHeaders(headers);
    }

    public DefaultDynamicTarget(Class<T> type, String serviceName, String url, Map<String, Object> headers) {
        this.setType(type);
        this.setServiceName(serviceName);
        this.setUrl(url);
        this.setHeaders(headers);
    }

    public DefaultDynamicTarget() {
    }

    @Override
    public Class<T> type() {
        return this.getType();
    }

    @Override
    public String name() {
        return this.getServiceName();
    }

    @Override
    public String url() {
        return this.getUrl();
    }

    @Override
    public Request apply(RequestTemplate input) {
        if (input.url().indexOf("http") != 0) {
            input.insert(0, this.url());
        }

        if (this.headers != null && !this.headers.isEmpty()) {
            Iterator<String> keys = this.headers.keySet().iterator();
            String key = null;

            while(keys.hasNext()) {
                key = (String)keys.next();
                input.header(key, new String[]{String.valueOf(this.headers.get(key))});
            }
        }

        return input.request();
    }
}
