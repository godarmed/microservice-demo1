package com.godarmed.core.starters.feignwrapper.config.multipart;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;

import java.io.OutputStream;

public class HttpOutputMessageImpl implements HttpOutputMessage {
    private OutputStream body;
    private HttpHeaders headers;

    public HttpOutputMessageImpl(OutputStream body, HttpHeaders headers) {
        this.body = body;
        this.headers = headers;
    }

    @Override
    public OutputStream getBody() {
        return this.body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.headers;
    }
}
