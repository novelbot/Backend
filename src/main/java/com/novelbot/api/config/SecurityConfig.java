package com.novelbot.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호 비활성화 (Stateless한 REST API에서는 일반적으로 비활성화)
                .csrf(csrf -> csrf.disable())
                // 모든 HTTP 요청에 대해 접근을 허용하도록 설정
                // 향후 API 명세에 따라 특정 경로에 대한 인증/인가 요구사항을 추가해야 합니다.
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());

        return http.build();
    }

    // @Bean
    // public BCryptPasswordEncoder passwordEncoder() {
    // return new BCryptPasswordEncoder();
    // }
}
