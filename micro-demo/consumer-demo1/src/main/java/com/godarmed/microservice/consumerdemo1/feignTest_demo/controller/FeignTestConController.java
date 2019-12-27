package com.godarmed.microservice.consumerdemo1.feignTest_demo.controller;

import com.godarmed.core.starters.global.entity.ResultModel;
import com.godarmed.core.starters.global.entity.UserViews;
import com.godarmed.microservice.protocol.feigntestprotocol.protocol.FeignTestServiceFeign;
import feign.Response;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/feignTest")
public class FeignTestConController {

    @Autowired
    FeignTestServiceFeign feignTestServiceFeign;

    @RequestMapping("/getBasicype")
    public String getBasicType(@RequestBody String basicType) {
        return feignTestServiceFeign.getBasicType(basicType);
    }


    @RequestMapping("/getBasicObject")
    public UserViews getBasicObject(@RequestBody UserViews userViews) {
        return feignTestServiceFeign.getBasicObject(userViews);
    }


    @RequestMapping("/getMultipartFile")
    public void getMultipartFile(HttpServletResponse response) {
        Response source = feignTestServiceFeign.getMultipartFile();
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(response.getOutputStream());
            IOUtils.copy(source.body().asInputStream(), out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @RequestMapping("/transMultipartFile")
    public ResultModel<Boolean> transMultipartFile(MultipartFile multipartFile) {
        return null;
    }




    @RequestMapping("/transMultipartFileArray")
    public ResultModel<Boolean> transMultipartFileArray(MultipartFile[] multipartFile) {
        return null;
    }




    @RequestMapping("/transMultipartFileList")
    public ResultModel<Boolean> transMultipartFileList(List<MultipartFile> multipartFile) {
        return null;
    }


}
