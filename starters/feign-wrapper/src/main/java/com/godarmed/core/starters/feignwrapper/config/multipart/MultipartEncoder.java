package com.godarmed.core.starters.feignwrapper.config.multipart;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MultipartEncoder implements Encoder {
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final Class<MultipartFile[]> MULTIPART_ARRAY_CLAZZ = MultipartFile[].class;
    private static final String FILES_KEY = "multipartFiles";
    private final List<HttpMessageConverter<?>> converters = (new RestTemplate()).getMessageConverters();
    private final HttpHeaders multipartHeaders = new HttpHeaders();
    private final HttpHeaders jsonHeaders = new HttpHeaders();

    public MultipartEncoder() {
        this.multipartHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        this.jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (isFormRequest(bodyType)) {
            this.encodeMultipartFormRequest(object, template);
        } else {
            this.encodeRequest(object, this.jsonHeaders, template);
        }

    }

    private void encodeMultipartFormRequest(Object object, RequestTemplate template) throws EncodeException {
        if (object == null) {
            throw new EncodeException("Cannot encode request with null form.");
        } else {
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap();
            if (this.isMultipartFile(object)) {
                MultipartFile multipartFile = (MultipartFile) object;
                map.add(multipartFile.getName(), this.encodeMultipartFile(multipartFile));
            } else if (this.isMultipartFileArray(object)) {
                this.encodeMultipartFiles(map, "multipartFiles", Arrays.asList((MultipartFile[]) ((MultipartFile[]) object)));
            } else {
                map.add("", this.encodeJsonObject(object));
            }

            this.encodeRequest(map, this.multipartHeaders, template);
        }
    }

    private boolean isMultipartFile(Object object) {
        return object instanceof MultipartFile;
    }

    private boolean isMultipartFileArray(Object o) {
        return o != null && o.getClass().isArray() && MultipartFile.class.isAssignableFrom(o.getClass().getComponentType());
    }

    private HttpEntity<?> encodeMultipartFile(MultipartFile file) {
        HttpHeaders filePartHeaders = new HttpHeaders();
        filePartHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        try {
            Resource multipartFileResource = new MultipartFileResource(file.getOriginalFilename(), file.getSize(), file.getInputStream());
            return new HttpEntity(multipartFileResource, filePartHeaders);
        } catch (IOException var4) {
            throw new EncodeException("Cannot encode request.", var4);
        }
    }

    private void encodeMultipartFiles(LinkedMultiValueMap<String, Object> map, String name, List<? extends MultipartFile> files) {
        HttpHeaders filePartHeaders = new HttpHeaders();
        filePartHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        try {
            Iterator var5 = files.iterator();

            while (var5.hasNext()) {
                MultipartFile file = (MultipartFile) var5.next();
                Resource multipartFileResource = new MultipartFileResource(file.getOriginalFilename(), file.getSize(), file.getInputStream());
                map.add(name, new HttpEntity(multipartFileResource, filePartHeaders));
            }

        } catch (IOException var8) {
            throw new EncodeException("Cannot encode request.", var8);
        }
    }

    private HttpEntity<?> encodeJsonObject(Object o) {
        HttpHeaders jsonPartHeaders = new HttpHeaders();
        jsonPartHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(o, jsonPartHeaders);
    }

    private void encodeRequest(Object value, HttpHeaders requestHeaders, RequestTemplate template) throws EncodeException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HttpOutputMessageImpl dummyRequest = new HttpOutputMessageImpl(outputStream, requestHeaders);

        try {
            Class<?> requestType = value.getClass();
            MediaType requestContentType = requestHeaders.getContentType();
            Iterator var8 = this.converters.iterator();

            while (var8.hasNext()) {
                HttpMessageConverter<Object> messageConverter = (HttpMessageConverter) var8.next();
                if (messageConverter.canWrite(requestType, requestContentType)) {
                    messageConverter.write(value, requestContentType, dummyRequest);
                    break;
                }
            }
        } catch (IOException var10) {
            throw new EncodeException("Cannot encode request.", var10);
        }

        HttpHeaders headers = dummyRequest.getHeaders();
        if (headers != null) {
            Iterator var12 = headers.entrySet().iterator();

            while (var12.hasNext()) {
                Map.Entry<String, List<String>> entry = (Map.Entry) var12.next();
                template.header((String) entry.getKey(), (Iterable) entry.getValue());
            }
        }

        template.body(outputStream.toByteArray(), DEFAULT_CHARSET);
    }

    private static boolean isFormRequest(Type type) {
        return MAP_STRING_WILDCARD.equals(type) || MULTIPART_ARRAY_CLAZZ.equals(type);
    }
}
