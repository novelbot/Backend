package com.novelbot.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.auth.LoginRequest;
import com.novelbot.api.dto.auth.LoginResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // TODO: 로그인 비즈니스 로직 처리 (Service 호출)
        // 예: String token = authService.login(loginRequest);
        // LoginResponse response = new LoginResponse(token);
        // return ResponseEntity.ok(response);
        return null; // 임시 반환
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // TODO: 로그아웃 처리 (예: 토큰 무효화)
        return ResponseEntity.noContent().build();
    }
}