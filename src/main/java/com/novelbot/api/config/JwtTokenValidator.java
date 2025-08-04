package com.novelbot.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.novelbot.api.service.auth.UserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenValidator {

    private final UserDetailsService userDetailsService;

    public JwtTokenValidator(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 토큰");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원하지 않는 토큰");
        } catch (MalformedJwtException e) {
            System.out.println("잘못된 형식의 토큰");
        } catch (SignatureException e) {
            System.out.println("서명 불일치 (위조)");
        } catch (IllegalArgumentException e) {
            System.out.println("토큰이 null 또는 빈 문자열");
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
