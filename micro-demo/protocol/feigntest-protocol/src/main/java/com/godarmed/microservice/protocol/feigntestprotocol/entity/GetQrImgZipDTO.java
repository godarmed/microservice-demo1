package com.godarmed.microservice.protocol.feigntestprotocol.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GetQrImgZipDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    private String zipName;

    private List<String> urls;

    public GetQrImgZipDTO() {
    }

    public GetQrImgZipDTO(String zipName, List<String> urls) {
        this.zipName = zipName;
        this.urls = urls;
    }
}
