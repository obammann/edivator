package com.gruppe4b.edivator.backend.configuration;

import com.gruppe4b.edivator.backend.fileUpload.GMultipartResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;

@Configuration
public class BeanConfiguration {

    @Bean
    public MultipartResolver multipartResolver(){
//        return new CommonsMultipartResolver();
        return new GMultipartResolver();
    }

}
