package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.join.UserCreateRequest;
import com.novelbot.api.dto.user.UserResponseDto;
import com.novelbot.api.dto.user.UserUpdateDto;
import com.novelbot.api.service.join.RegistrationService;
import com.novelbot.api.service.join.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/users")
public class UserController {

    private final RegistrationService registor;
    private final UserService userService;

    public UserController(RegistrationService registor, UserService userService) {
        this.registor = registor;
        this.userService = userService;
    }

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 - 유효하지 않은 사용자 정보"),
            @ApiResponse(responseCode = "409", description = "충돌 - 이미 존재하는 사용자")
    })
    @PostMapping
    public ResponseEntity<Void> signup(@RequestBody UserCreateRequest userCreate) {
        registor.registerUser(userCreate);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "사용자 정보 조회", description = "JWT 토큰을 통해 사용자 정보를 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/user")
    public ResponseEntity<UserResponseDto> getUserInfo(@RequestHeader("Authorization") String token) {
        UserResponseDto response = userService.getUserInfo(token);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 정보 수정", description = "JWT 토큰을 통해 사용자 정보를 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "중복된 사용자명 또는 이메일")
    })
    @PostMapping("/user")
    public ResponseEntity<Void> updateUserInfo(@RequestHeader("Authorization") String token, @RequestBody UserUpdateDto userUpdate) {
        userService.updateUserInfo(token, userUpdate);
        return ResponseEntity.ok().build();
    }

    // @DeleteMapping("/{userId}")
    // public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
    //     userService.deleteUser(userId);
    //     return ResponseEntity.noContent().build();
    // }
}