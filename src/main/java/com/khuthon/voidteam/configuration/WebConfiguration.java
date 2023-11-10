package com.khuthon.voidteam.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:8080")
                .allowedHeaders("authorization", "User-Agent", "Cache-Control", "Content-Type", "Access-Control-Allow-Credentials", "my-custom-header")
                .exposedHeaders("authorization", "User-Agent", "Cache-Control", "Content-Type", "Access-Control-Allow-Credentials","my-custom-header")
                .allowCredentials(true)
                .allowedMethods("*");
    }


}