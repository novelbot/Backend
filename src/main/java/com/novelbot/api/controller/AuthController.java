package com.novelbot.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.auth.LoginRequest;
import com.novelbot.api.dto.auth.LoginResponse;
import com.novelbot.api.service.auth.LoginService;
import com.novelbot.api.service.auth.LogoutService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;
    private final LogoutService logoutService;

    public AuthController(LoginService loginService, LogoutService logoutService) {
        this.loginService = loginService;
        this.logoutService = logoutService;
    }


    @Operation(summary = "로그인", description = "사용자 로그인 API")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = loginService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 API")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(String token) {
        logoutService.logout(token);
        return ResponseEntity.noContent().build();
    }
}