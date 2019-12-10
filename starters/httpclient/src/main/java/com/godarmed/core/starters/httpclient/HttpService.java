package com.godarmed.core.starters.httpclient;

import com.alibaba.fastjson.JSONReader;
import com.godarmed.core.starters.httpclient.exceptions.RemoteException;
import com.godarmed.core.starters.httpclient.exceptions.RemoteExceptionEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class HttpService {
    private static final Logger log = LogManager.getLogger(HttpService.class);
    @Autowired
    RestTemplate restTemplate;

    public HttpService() {
    }

    public JSONReader requestForJSONReader(String url, HttpMethod httpMethod, HttpHeaders httpHeaders, Object param) {
        HttpEntity request = new HttpEntity(param, httpHeaders);

        try {
            ResponseEntity<Resource> entity = null;
            if (httpMethod.equals(HttpMethod.GET)) {
                entity = this.restTemplate.getForEntity(url, Resource.class, new Object[]{param});
            } else {
                entity = this.restTemplate.postForEntity(url, request, Resource.class, new Object[0]);
            }

            if (entity.getStatusCode().equals(HttpStatus.OK)) {
                InputStream inputStream = ((Resource)entity.getBody()).getInputStream();
                return new JSONReader(new InputStreamReader(inputStream, Charset.forName("utf-8")));
            }
        } catch (Exception var8) {
            var8.printStackTrace();
            log.error(var8.getMessage());
        }

        return null;
    }

    public <T> ResponseEntity<T> request(String url, HttpMethod httpMethod, HttpHeaders httpHeaders, Object param, Class<T> returnType, int retries) throws Exception {
        HttpEntity request = new HttpEntity(param, httpHeaders);

        try {
            return this.restTemplate.exchange(url, httpMethod, request, returnType, new Object[0]);
        } catch (Exception var9) {
            if (retries > 0) {
                log.error("request[" + url + "] error for: " + var9.getMessage() + ", now retry request");
                return this.request(url, httpMethod, httpHeaders, param, returnType, retries - 1);
            } else {
                throw new RemoteException(RemoteExceptionEnum.UNKNOWN_ERROR, var9.getMessage());
            }
        }
    }

    public <T> ResponseEntity<T> request(String url, HttpMethod httpMethod, Map<String, Object> headers, Object param, Class<T> returnType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach((key, value) -> {
                if (value instanceof List) {
                    httpHeaders.put(key, (List)value);
                } else {
                    httpHeaders.put(key, Arrays.asList(String.valueOf(value)));
                }

            });
        }

        try {
            return this.request(url, httpMethod, httpHeaders, param, returnType, 0);
        } catch (Exception var8) {
            throw new RemoteException(RemoteExceptionEnum.UNKNOWN_ERROR, var8.getMessage());
        }
    }

    public <T> ResponseEntity<T> post(String url, Map<String, Object> headers, Object param, Class<T> returnType) {
        return this.request(url, HttpMethod.POST, headers, param, returnType);
    }

    public <T> ResponseEntity<T> post(String url, Object param, Class<T> returnType) {
        return this.request(url, HttpMethod.POST, (Map)null, param, returnType);
    }

    public <T> ResponseEntity<T> get(String url, Map<String, Object> headers, Class<T> returnType) {
        return this.request(url, HttpMethod.GET, headers, (Object)null, returnType);
    }

    public <T> ResponseEntity<T> get(String url, Class<T> returnType) {
        return this.request(url, HttpMethod.GET, (Map)null, (Object)null, returnType);
    }

    public HttpHeaders getHeaderFromRequest(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(request.getContentType()));
        List<String> cookies = this.buildCookies(request.getCookies());
        if (cookies != null && cookies.size() > 0) {
            headers.put("Cookie", cookies);
        }

        return headers;
    }

    private List<String> buildCookies(Cookie[] cookies) {
        List<String> scookies = new ArrayList();
        Cookie[] var6 = cookies;
        int var5 = cookies.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            Cookie cookie = var6[var4];
            scookies.add(cookie.getName() + "=" + cookie.getValue());
        }

        return scookies;
    }
}