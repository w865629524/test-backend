package com.zq.backend.config;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class BeanConfig {

    // 配置 PasswordEncoder
    // 配置密码加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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

    @Value("${oss.access.key.id}")
    private String accessKeyId;

    @Value("${oss.access.key.secret}")
    private String accessKeySecret;

    @Value("${oss.region}")
    private String region;

    @Value("${oss.endpoint}")
    private String endpoint;

    @Bean(destroyMethod = "shutdown")
    public OSS ossClient() {
        DefaultCredentialProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        return OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
    }
}
