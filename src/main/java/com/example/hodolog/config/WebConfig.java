package com.example.hodolog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins("http://localhost:8081", "http://172.17.0.1:8081");
                .allowedOrigins("*")
                .allowedMethods("OPTIONS", "GET", "POST");
    }
}
