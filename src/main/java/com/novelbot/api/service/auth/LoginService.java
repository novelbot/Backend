package com.novelbot.api.service.auth;

import com.novelbot.api.config.JwtTokenProvider;
import com.novelbot.api.domain.User;
import com.novelbot.api.dto.auth.LoginRequest;
import com.novelbot.api.dto.auth.LoginResponse;
import com.novelbot.api.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
                    HttpStatus.NOT_FOUND, "Error Code: 404, Unauthorized(가입되지 않은 사용자 입니다)"
            );
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword().trim(), user.getUserPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Error Code: 401, Unauthorized(잘못된 비밀번호입니다)"
            );
        }

        try {
            String token = jwtTokenProvider.generateToken(user.getUserName());
            return new LoginResponse(token);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error Code: 500, Internal Server Error(JWT 토큰 생성 중 오류 발생: " + e.getMessage() + ")"
            );
        }
    }
}
