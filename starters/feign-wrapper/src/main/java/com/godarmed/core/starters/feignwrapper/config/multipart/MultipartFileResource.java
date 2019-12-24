package com.godarmed.core.starters.feignwrapper.config.multipart;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;

public class MultipartFileResource extends InputStreamResource {
    private String filename;
    private long size;

    public MultipartFileResource(String filename, long size, InputStream inputStream) {
        super(inputStream);
        this.size = size;
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public InputStream getInputStream() throws IOException, IllegalStateException {
        return super.getInputStream();
    }

    @Override
    public long contentLength() {
        return this.size;
    }
}
