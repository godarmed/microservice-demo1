package com.godarmed.core.starters.global.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

@Data
public class HeaderEntity {
    public static final String REQUEST_LOCAL_ID = "requestLocalId";
    public static final String REQUEST_ID = "requestId";
    public static final String BUSINESS_ID = "businessId";
    private String requestLocalId;
    private String requestId;
    private String businessId;

    public HeaderEntity(Map<String, Collection<String>> headers) {
        this.requestLocalId = this.getByKey(headers, "requestLocalId");
        this.requestId = this.getByKey(headers, "requestId");
        this.businessId = this.getByKey(headers, "businessId");
    }

    public HeaderEntity(HttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        this.requestLocalId = this.getByKey(headers, "requestLocalId");
        this.requestId = this.getByKey(headers, "requestId");
        this.businessId = this.getByKey(headers, "businessId");
    }

    private String getByKey(HttpHeaders headers, String key) {
        List<String> Locals = headers.get(key);
        return Locals != null && Locals.size() > 0 ? (String)Locals.get(0) : "";
    }

    private String getByKey(Map<String, Collection<String>> headers, String key) {
        Collection<String> locals = (Collection)headers.get(key.toLowerCase());
        if (locals != null && locals.size() > 0) {
            return ((String[])locals.toArray(new String[locals.size()]))[0];
        } else {
            locals = (Collection)headers.get(key);
            return locals != null && locals.size() > 0 ? ((String[])locals.toArray(new String[locals.size()]))[0] : "";
        }
    }
}

