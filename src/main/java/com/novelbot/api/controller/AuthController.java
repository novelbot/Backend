package com.novelbot.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.auth.LoginRequest;
import com.novelbot.api.dto.auth.LoginResponse;
import com.novelbot.api.service.auth.LoginService;
import com.novelbot.api.service.auth.LogoutService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;
    private final LogoutService logoutService;

    public AuthController(LoginService loginService, LogoutService logoutService) {
        this.loginService = loginService;
        this.logoutService = logoutService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // TODO: 로그인 비즈니스 로직 처리 (Service 호출)
        // 예: String token = authService.login(loginRequest);
        // LoginResponse response = new LoginResponse(token);
        // return ResponseEntity.ok(response);
        loginService.login(loginRequest);
        return null; // 임시 반환
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(String token) {
        // TODO: 로그아웃 처리 (예: 토큰 무효화)
        logoutService.logout(token);
        return ResponseEntity.noContent().build();
    }
}