package com.doc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // DO NOT include context path here
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/C:/Users/Aniket/Maid_For_you_Portal2/Secure_Document_Sharing/uploads/");
    }
}
