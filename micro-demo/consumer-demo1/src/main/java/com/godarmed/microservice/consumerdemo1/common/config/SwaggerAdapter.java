package com.godarmed.microservice.consumerdemo1.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class SwaggerAdapter implements WebMvcConfigurer {



//	upload:
//	prefix: /images/**
// absolute-path: D:/images/


	@Value("${upload.prefix}")
	private String prefix;


	@Value("${upload.absolute-path}")
	private String absolutePath;



	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("docs.html")
		//		.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("doc.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");

		//设置tomcat
		registry.addResourceHandler(prefix) //设置拦截器
                .addResourceLocations("file:"+absolutePath+ File.separator); //指定磁盘绝对路径
	}
}
