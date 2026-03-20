package org.padalustro.security;  // o ponlo en config o presentation/config, es mejor

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")                     // Para dev está bien, luego cámbialo a orígenes concretos
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                        .allowedHeaders("*")                     // Content-Type, Authorization, etc.
                        .exposedHeaders("Content-Disposition")   // útil si devuelves ficheros
                        .allowCredentials(false)                  // importante si usas cookies o auth con credenciales
                        .maxAge(3600);                           // cache del preflight 1h
            }
        };
    }
}