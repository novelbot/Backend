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
        LoginResponse response = loginService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(String token) {
        logoutService.logout(token);
        return ResponseEntity.noContent().build();
    }
}