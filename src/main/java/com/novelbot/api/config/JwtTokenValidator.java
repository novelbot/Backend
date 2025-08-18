package com.novelbot.api.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.novelbot.api.service.auth.UserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenValidator {

    private final UserDetailsService userDetailsService;
    private final SecretKey secretKey;

    public JwtTokenValidator(@Value("${jwt.secret}") String jwtSecret, UserDetailsService userDetailsService) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.userDetailsService = userDetailsService;
    }

    private String extractToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    public String getUsername(String token) {
        token = extractToken(token);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            // 로그 찍고, 필요 시 예외 던짐
            throw new RuntimeException("잘못된 토큰", e);
        }
    }

    public Integer getUserId(String token) {
        token = extractToken(token);
        try {
            return getClaims(token).get("userId", Integer.class);
        } catch (JwtException e) {
            // 로그를 남기거나, 애플리케이션의 요구사항에 맞게 예외를 처리할 수 있습니다.
            throw new RuntimeException("잘못된 토큰에서 userId를 추출할 수 없습니다.", e);
        }
    }

    public boolean validateToken(String token) {
        token = extractToken(token);
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.out.println("유효하지 않은 토큰: " + e.getMessage());
            return false;
        }
    }

    public Claims getClaims(String token) {
        token = extractToken(token);
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        token = extractToken(token);
        Integer userId = getUserId(token);

        UserDetails userDetails = userDetailsService.loadUserByUserId(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
