package com.novelbot.api.service.auth;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.novelbot.api.config.JwtTokenValidator;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class LogoutService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenValidator jwtTokenValidator;

    // Redis가 없을 때 사용할 인메모리 블랙리스트 (테스트용)
    private final Set<String> inMemoryBlacklist = new HashSet<>();

    private static final String TOKEN_BLACKLIST_PREFIX = "blacklist:";

    public LogoutService(@Autowired(required = false) RedisTemplate<String, String> redisTemplate, 
                        JwtTokenValidator jwtTokenValidator) {
        this.redisTemplate = redisTemplate;
        this.jwtTokenValidator = jwtTokenValidator;
    }

    public void logout(String token) {
        if (token == null || token.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰이 비어 있습니다");
        }

        if (!jwtTokenValidator.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "만료되었거나 유효하지 않은 토큰입니다");
        }

        Claims claims = jwtTokenValidator.getClaims(token);
        Date expiration = claims.getExpiration();
        long ttl = expiration.getTime() - System.currentTimeMillis();

        if (ttl <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 만료된 토큰입니다");
        }

        // Redis가 사용 가능하면 Redis 사용, 아니면 인메모리 사용 (로컬 테스트용)
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + token, "blacklisted", ttl, TimeUnit.MILLISECONDS);
        } else {
            // 로컬 테스트 환경에서는 인메모리로 처리
            inMemoryBlacklist.add(token);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        if (redisTemplate != null) {
            return redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + token);
        } else {
            return inMemoryBlacklist.contains(token);
        }
    }
}
