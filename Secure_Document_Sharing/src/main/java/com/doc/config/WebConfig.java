package com.doc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	
	 @Value("${upload.path}")
	    private String uploadPath;
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // DO NOT include context path here
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:"+ uploadPath);
    }
}
