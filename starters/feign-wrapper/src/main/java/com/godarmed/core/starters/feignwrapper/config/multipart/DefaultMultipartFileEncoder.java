package com.godarmed.core.starters.feignwrapper.config.multipart;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.ContentType;
import feign.form.FormEncoder;
import feign.form.MultipartFormContentProcessor;
import feign.form.spring.SpringManyMultipartFilesWriter;
import feign.form.spring.SpringSingleMultipartFileWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 自定义文件编码器（Feign上传用）
 * 解决feign同时传输（多）文件和参数的问题
 */
@Slf4j
public class DefaultMultipartFileEncoder extends FormEncoder {
    //用于获取MultipartFile的参数化类型
    private List<MultipartFile> multipartFileList;

    /**
     * Constructor with the default Feign's encoder as a delegate.
     */
    public DefaultMultipartFileEncoder() {
        this(new Default());
    }


    /**
     * Constructor with specified delegate encoder.
     *
     * @param delegate delegate encoder, if this encoder couldn't encode object.
     */
    public DefaultMultipartFileEncoder(Encoder delegate) {
        super(delegate);

        MultipartFormContentProcessor processor = (MultipartFormContentProcessor) getContentProcessor(ContentType.MULTIPART);
        processor.addWriter(new SpringSingleMultipartFileWriter());
        processor.addWriter(new SpringManyMultipartFilesWriter());
    }


    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        Type multipartFileListType = getListType(DefaultMultipartFileEncoder.class, "multipartFileList");
//        log.info("List参数类型[]",multipartFileListType);

        if (bodyType.equals(MultipartFile.class)) {
            MultipartFile file = (MultipartFile) object;
            Map data = Collections.singletonMap(file.getName(), object);
            super.encode(data, MAP_STRING_WILDCARD, template);
            return;
        } else if (bodyType.equals(MultipartFile[].class)) {
            MultipartFile[] file = (MultipartFile[]) object;
            if (file != null) {
                Map data = Collections.singletonMap(file.length == 0 ? "" : file[0].getName(), object);
                super.encode(data, MAP_STRING_WILDCARD, template);
                return;
            }
        } else if (bodyType.equals(multipartFileListType)) {
            List<MultipartFile> file = (List<MultipartFile>) object;
            if (file != null) {
                Map data = Collections.singletonMap(file.size() == 0 ? "" : file.get(0).getName(), object);
                super.encode(data, MAP_STRING_WILDCARD, template);
                return;
            }
        }
        super.encode(object, bodyType, template);
    }

    /**
     * 获取特定类型的List的参数化类型
     */
    private Type getListType(Class clazz,String fieldName) {
        Type listType = null;
        Field listField = null;
        try {
            listField = clazz.getDeclaredField(fieldName);
            if(listField != null){
                listType = listField.getGenericType();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return listType;
    }


}
