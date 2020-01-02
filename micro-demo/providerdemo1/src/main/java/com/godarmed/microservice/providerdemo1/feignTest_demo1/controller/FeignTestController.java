package com.godarmed.microservice.providerdemo1.feignTest_demo1.controller;

import cn.hutool.Hutool;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.godarmed.core.starters.global.entity.ResultModel;
import com.godarmed.core.starters.global.entity.UserViews;
import com.godarmed.microservice.protocol.feigntestprotocol.entity.GetQrImgZipDTO;
import com.godarmed.microservice.protocol.feigntestprotocol.protocol.FeignTestServiceFeign;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FeignTestController{

    @RequestMapping("/feignTest/getBasicype")
    public String getBasicType(@Param("basicType") String basicType){
        return basicType;
    };


    @RequestMapping("/feignTest/getBasicObject")
    public UserViews getBasicObject(@RequestBody UserViews userViews){
        return userViews;
    };


    @RequestMapping("/feignTest/getMultipartFile")
    public void getMultipartFile(HttpServletResponse response){
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(response.getOutputStream());

            QrConfig config = new QrConfig(300, 300);
            // 设置边距，既二维码和背景之间的边距
            config.setMargin(3);
            // 设置前景色，既二维码颜色（青色）
            config.setForeColor(Color.CYAN.getRGB());
            // 设置背景色（灰色）
            config.setBackColor(Color.GRAY.getRGB());

            // 生成二维码到文件，也可以到流
            QrCodeUtil.generate("http://hutool.cn/", config,"jpg", out);

            //输出文件
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping("/feignTest/getMultipartFiles")
    public Map<String,byte[]> getMultipartFiles(@RequestBody GetQrImgZipDTO request){
        Map<String,byte[]> imgs = new HashMap<>();
        //生成文件
        for (String url : request.getUrls()) {
            byte[] qrImg = generateQrCode(url);
            if(!url.endsWith(".jpeg") && !url.endsWith(".jpg")){
                imgs.put(url+".jpeg",qrImg);
                continue;
            }
            imgs.put(url,qrImg);
        }
        return imgs;
    }




    @PostMapping("/feignTest/transMultipartFile")
    public ResultModel<Boolean> transMultipartFile(@RequestPart MultipartFile multipartFile){
        ResultModel<Boolean> resultModel = new ResultModel<>();
        File file = new File(multipartFile.getName()+".jpeg");
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        resultModel.setData(true);
        return resultModel;
    }


    @RequestMapping("/feignTest/transMultipartFileArray")
    public ResultModel<Boolean> transMultipartFileArray(MultipartFile[] multipartFile){
        return null;
    }


    @RequestMapping("/feignTest/transMultipartFileList")
    public ResultModel<Boolean> transMultipartFileList(List<MultipartFile> multipartFile){
        return null;
    }

    private byte[] generateQrCode(String url){
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        QrConfig config = new QrConfig(500, 500);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(3);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(Color.CYAN.getRGB());
        // 设置背景色（灰色）
        config.setBackColor(Color.GRAY.getRGB());

        // 生成二维码到文件，也可以到流
        QrCodeUtil.generate(url, config,"jpg", out);

        return out.toByteArray();
    }
}
