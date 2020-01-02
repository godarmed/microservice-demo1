package com.godarmed.microservice.consumerdemo1.feignTest_demo.controller;

import com.godarmed.core.starters.global.entity.ResultModel;
import com.godarmed.core.starters.global.entity.UserViews;
import com.godarmed.microservice.protocol.feigntestprotocol.entity.GetQrImgZipDTO;
import com.godarmed.microservice.protocol.feigntestprotocol.protocol.FeignTestServiceFeign;
import feign.Param;
import feign.Response;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/feignTest")
public class FeignTestConController {

    @Autowired
    FeignTestServiceFeign feignTestServiceFeign;

    @RequestMapping("/getBasicype")
    public String getBasicType(String basicType) {
        String back = feignTestServiceFeign.getBasicType(basicType);
        return back;
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

    @RequestMapping("/getMultipartFiles")
    public void getMultipartFiles(String zipName,String[] urls, HttpServletResponse response) {
        GetQrImgZipDTO request = new GetQrImgZipDTO();
        request.setZipName(zipName);
        request.setUrls(Arrays.asList(urls));
        Map<String,byte[]> source = feignTestServiceFeign.getMultipartFiles(request);
        responseZip(request.getZipName(),source,response);
    }


    @RequestMapping("/transMultipartFile")
    public ResultModel<Boolean> transMultipartFile(MultipartFile multipartFile) {
        return feignTestServiceFeign.transMultipartFile(multipartFile);
    }




    @RequestMapping("/transMultipartFileArray")
    public ResultModel<Boolean> transMultipartFileArray(MultipartFile[] multipartFile) {
        return null;
    }




    @RequestMapping("/transMultipartFileList")
    public ResultModel<Boolean> transMultipartFileList(List<MultipartFile> multipartFile) {
        return null;
    }

    private void responseZip(String zipName, Map<String,byte[]> files, HttpServletResponse response){
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try{
            response.reset();
            response.setContentType("application/zip;charset=UTF-8");
            //response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-disposition", "attachment;filename="+  new String(zipName.getBytes("utf-8"),"iso-8859-1"));


            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            bos = new BufferedOutputStream(zos);

            for(Map.Entry<String, byte[]> entry : files.entrySet()){
                String fileName = URLEncoder.encode(entry.getKey(),"utf-8"); //每个zip文件名
                byte[] file = entry.getValue(); //这个zip文件的字节

                bis = new BufferedInputStream(new ByteArrayInputStream(file));
                zos.putNextEntry(new ZipEntry(fileName));
                IOUtils.copy(bis,bos);

                bis.close();
                bos.flush();
            }
            bos.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
