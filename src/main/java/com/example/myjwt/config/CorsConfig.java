package com.example.myjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);//서버가 응답을 할 때 front에서 처리할 수 있게 설정 ajax로 요청했지만 false면 응답이 안감.
        config.addAllowedOrigin("*"); //모든 ip에 응답허용
        config.addAllowedHeader("*"); //모든 헤더에 응답허용
        config.addAllowedMethod("*"); //모든 http 메소드에 응답허용
        source.registerCorsConfiguration("/api/**",config);
        return new CorsFilter(source);
    }

}
