package com.zq.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 设置允许的来源（替换为你的前端地址）
//        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedOriginPatterns(List.of("*"));
        // 允许的 HTTP 方法（必须包含 OPTIONS）
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许的请求头
//        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowedHeaders(List.of("*"));
        // 是否允许携带凭证（如 Cookie）
        config.setAllowCredentials(true);
        // 预检请求缓存时间（单位：秒）
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 对所有路径生效
        return source;
    }
}