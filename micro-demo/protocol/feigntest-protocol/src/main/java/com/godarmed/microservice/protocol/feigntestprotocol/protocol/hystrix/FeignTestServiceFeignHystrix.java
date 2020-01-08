package com.godarmed.microservice.protocol.feigntestprotocol.protocol.hystrix;

import com.godarmed.core.starters.feignwrapper.fallbacks.IHystrix;
import com.godarmed.core.starters.global.entity.ResultModel;
import com.godarmed.core.starters.global.entity.UserViews;
import com.godarmed.core.starters.system.exception.errors.BaseHandlerException;
import com.godarmed.microservice.protocol.feigntestprotocol.entity.GetQrImgZipDTO;
import com.godarmed.microservice.protocol.feigntestprotocol.protocol.FeignTestServiceFeign;
import feign.Response;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Log4j2
public class FeignTestServiceFeignHystrix implements FeignTestServiceFeign, IHystrix {

    private Throwable throwable;

    @Override
    public Throwable setThrowable(Throwable var1) {
        log.error(throwable.getMessage());
        return null;
    }

    @Override
    public String getBasicType(String basicType) {
        throw new BaseHandlerException(500, throwable == null ? "未知异常" : throwable.getMessage());
    }

    @Override
    public UserViews getBasicObject(UserViews userViews) {
        throw new BaseHandlerException(500, throwable == null ? "未知异常" : throwable.getMessage());
    }

    @Override
    public Response getMultipartFile(String fileName) {
        throw new BaseHandlerException(500, throwable == null ? "未知异常" : throwable.getMessage());
    }

    @Override
    public Map<String, byte[]> getMultipartFiles(GetQrImgZipDTO request) {
        throw new BaseHandlerException(500, throwable == null ? "未知异常" : throwable.getMessage());
    }

    @Override
    public ResultModel<Boolean> transMultipartFile(MultipartFile multipartFile) {
        throw new BaseHandlerException(500, throwable == null ? "未知异常" : throwable.getMessage());
    }

    @Override
    public ResultModel<Boolean> transMultipartFileArray(MultipartFile[] multipartFile) {
        throw new BaseHandlerException(500, throwable == null ? "未知异常" : throwable.getMessage());
    }

    @Override
    public ResultModel<Boolean> transMultipartFileList(List<MultipartFile> multipartFile) {
        throw new BaseHandlerException(500, throwable == null ? "未知异常" : throwable.getMessage());
    }
}
