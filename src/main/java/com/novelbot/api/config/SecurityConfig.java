package com.novelbot.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http))
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 인증이 필요하지 않은 엔드포인트들
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/users").permitAll() // 회원가입
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/novels/**").permitAll() // 소설 조회 API들
                        .requestMatchers("/episodes/**").permitAll() // 에피소드 조회 API들
                        // 인증이 필요한 엔드포인트들
                        .requestMatchers("/users/user").authenticated() // 사용자 정보 조회/수정
                        .requestMatchers("/chatrooms/**").authenticated() // 채팅방 관련
                        .requestMatchers("/purchases/**").authenticated() // 구매 관련
                        .requestMatchers("/reading-progress/**").authenticated() // 독서 진도
                        .requestMatchers("/auth/logout").authenticated() // 로그아웃
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
