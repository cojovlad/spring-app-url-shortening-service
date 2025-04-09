package com.example.spring_app_url_shortening_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

@Configuration
public class ExceptionConfig {

    @Bean
    public SimpleMappingExceptionResolver exceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();

        Properties exceptionMappings = new Properties();
        exceptionMappings.put("com.yourpackage.exception.UrlNotFoundException", "/api/v1/dashboard");
        exceptionMappings.put("java.lang.Exception", "/api/v1/dashboard");

        resolver.setExceptionMappings(exceptionMappings);
        resolver.setDefaultErrorView("redirect:/api/v1/dashboard");
        return resolver;
    }
}