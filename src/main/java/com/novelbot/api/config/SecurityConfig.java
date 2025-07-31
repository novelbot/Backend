package com.novelbot.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 로그인, 회원가입 API는 누구나 접근 가능
                        .requestMatchers("/auth/login", "/users").permitAll()
                        // Github Actions의 Health Check를 위한 경로 허용
                        .requestMatchers("/actuator/health").permitAll()
                        // 위에서 지정한 경로 외의 모든 요청은 반드시 인증(로그인) 필요
                        .anyRequest().authenticated());

        // TODO: 향후 JWT 토큰을 검증하는 필터를 여기에 추가해야 합니다.

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
