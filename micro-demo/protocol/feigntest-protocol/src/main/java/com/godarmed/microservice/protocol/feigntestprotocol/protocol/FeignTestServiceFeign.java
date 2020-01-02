package com.godarmed.microservice.protocol.feigntestprotocol.protocol;

import com.godarmed.core.starters.feignwrapper.config.Feign;
import com.godarmed.core.starters.global.entity.ResultModel;
import com.godarmed.core.starters.global.entity.UserViews;
import com.godarmed.microservice.protocol.feigntestprotocol.entity.GetQrImgZipDTO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Feign(serviceName = "provider-8110")
public interface FeignTestServiceFeign {

    @RequestLine("GET /feignTest/getBasicype?basicType={basicType}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    public String getBasicType(@Param("basicType") String basicType);

    @RequestLine("POST /feignTest/getBasicObject")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    public UserViews getBasicObject(@RequestBody UserViews userViews);

    @RequestLine("GET /feignTest/getMultipartFile")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    public Response getMultipartFile(String fileName);

    @RequestLine("POST /feignTest/getMultipartFiles")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    public Map<String,byte[]> getMultipartFiles(@RequestBody GetQrImgZipDTO request);

    @RequestLine("POST /feignTest/transMultipartFile")
    @Headers({"Content-Type: multipart/form-data"})
    public ResultModel<Boolean> transMultipartFile(@Param("multipartFile") MultipartFile multipartFile);

    @RequestLine("GET /feignTest/transMultipartFileArray")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    public ResultModel<Boolean> transMultipartFileArray(@Param("multipartFile") MultipartFile[] multipartFile);

    @RequestLine("GET /feignTest/transMultipartFileList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    public ResultModel<Boolean> transMultipartFileList(@Param("multipartFile") List<MultipartFile> multipartFile);
}
