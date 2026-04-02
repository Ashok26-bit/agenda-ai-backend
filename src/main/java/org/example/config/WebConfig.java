package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Image-ah URL valiya (http://localhost:8080/uploads/...) pakka ithu mukkiyam
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:C:/uploads/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Netlify website unga computer-la irukura data-va edukka ithu dhaan "Permission"
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}