package com.novelbot.api.service.auth;

import com.novelbot.api.domain.User;
import com.novelbot.api.dto.auth.LoginRequest;
import com.novelbot.api.dto.auth.LoginResponse;
import com.novelbot.api.mapper.auth.LoginRequestDtoMapper;
import com.novelbot.api.repository.UserRepository;

//import io.jsonwebtoken.Jwts;
//import io.jsonwentoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private LoginRequestDtoMapper loginRequestDtoMapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        if (request.getUsername().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(사용자명을 입력해주세요)"
            );
        }
        if (request.getPassword().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(비밀번호를 입력해주세요)"
            );
        }

        Optional<User> optionalUser = userRepository.findByUserName(request.getUsername());

        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Error Code: 401, Unauthorized(가입되지 않은 사용자 입니다)"
            );
        }

        User user = optionalUser.get();
        LoginRequest loginRequest = loginRequestDtoMapper.toDto(optionalUser);

        if (!passwordEncoder.matches(request.getPassword().trim(), loginRequest.getPassword().trim())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Error Code: 401, Unauthorized(잘못된 비밀번호입니다)"
            );
        }

        LoginResponse response = new LoginResponse(null);

//        try {
//            return Jwts.builder()
//                    .setSubject(user.getUserEmail())
//                    .setIssuedAt(new Date())
//                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
//                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                    .compact();
//        } catch (Exception e) {
//            throw new ResponseStatusException(
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    "Error Code: 500, Internal Server Error(JWT 토큰 생성 중 오류 발생: " + e.getMessage() + ")"
//            );
//        }

        return response;
    }
}
