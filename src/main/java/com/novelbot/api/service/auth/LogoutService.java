package com.novelbot.api.service.auth;

//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class LogoutService {
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;

//    @Value("${jwt.secret}")
//    private String jwtSecret;
//
//    private static final String BLACKLIST_KEY = "jwt_blacklist";

    public void logout(String token) {
        // 입력 유효성 검사
        if (token == null || token.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Error Code: 400, Bad Request(토큰이 비어 있습니다)"
            );
        }

        try {
            // JWT 토큰 파싱 및 만료 시간 확인
//            Claims claims = Jwts.parser()
//                    .setSigningKey(jwtSecret)
//                    .parseClaimsJws(token)
//                    .getBody();
//            Date expiration = claims.getExpiration();
//            long ttl = expiration.getTime() - System.currentTimeMillis();
//
//            if (ttl <= 0) {
//                throw new ResponseStatusException(
//                        HttpStatus.BAD_REQUEST,
//                        "Error Code: 400, Bad Request(이미 만료된 토큰입니다)"
//                );
//            }

            // Redis에 토큰을 블랙리스트로 저장 (만료 시간까지)
//            redisTemplate.opsForSet().add(BLACKLIST_KEY, token);
//            redisTemplate.expire(BLACKLIST_KEY, ttl, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error Code: 500, Internal Server Error(로그아웃 처리 중 오류 발생: " + e.getMessage() + ")"
            );
        }
    }
}