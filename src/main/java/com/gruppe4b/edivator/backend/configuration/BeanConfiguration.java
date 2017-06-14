package com.gruppe4b.edivator.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class BeanConfiguration {

    @Bean
    public MultipartResolver multipartResolver(){
        return new CommonsMultipartResolver();
    }

}
