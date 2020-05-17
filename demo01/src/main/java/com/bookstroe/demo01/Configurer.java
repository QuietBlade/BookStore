package com.bookstroe.demo01;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.IOException;

@Configuration
public class Configurer implements WebMvcConfigurer {

    //映射本地图片路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File directory = new File("../../resources/static/img/");
        try{
            registry.addResourceHandler("/img/**").addResourceLocations("file:"+directory.getCanonicalPath());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //修改上传文件大小
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //指定文件大小
        factory.setMaxFileSize(DataSize.parse("5MB"));
        /// 设定上传文件大小
        factory.setMaxRequestSize(DataSize.parse("100MB"));
        return factory.createMultipartConfig();
    }
}

