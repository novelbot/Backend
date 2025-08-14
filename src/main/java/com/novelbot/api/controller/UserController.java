package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.join.UserCreateRequest;
import com.novelbot.api.dto.user.UserResponseDto;
import com.novelbot.api.dto.user.UserUpdateDto;
import com.novelbot.api.domain.User;
import com.novelbot.api.repository.UserRepository;
import com.novelbot.api.service.join.RegistrationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/users")
public class UserController {

    private final RegistrationService registor;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(RegistrationService registor, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.registor = registor;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public ResponseEntity<UserResponseDto> getUserInfo(Authentication authentication) {
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        UserResponseDto response = new UserResponseDto(
                user.getUserName(),
                user.getUserNickname(),
                user.getUserEmail()
        );
        
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
    public ResponseEntity<Void> updateUserInfo(Authentication authentication, @RequestBody UserUpdateDto userUpdate) {
        String currentUserName = authentication.getName();
        User user = userRepository.findByUserName(currentUserName)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 사용자명 중복 체크 (현재 사용자가 아닌 경우)
        if (userUpdate.getUserName() != null && !userUpdate.getUserName().equals(currentUserName)) {
            if (userRepository.existsUserByUserName(userUpdate.getUserName().trim())) {
                throw new RuntimeException("이미 존재하는 사용자명입니다.");
            }
        }
        
        // 이메일 중복 체크 (현재 사용자가 아닌 경우)
        if (userUpdate.getUserEmail() != null && !userUpdate.getUserEmail().equals(user.getUserEmail())) {
            if (userRepository.existsUsersByUserEmail(userUpdate.getUserEmail().trim())) {
                throw new RuntimeException("이미 존재하는 이메일입니다.");
            }
        }
        
        // 닉네임 중복 체크 (현재 사용자가 아닌 경우)
        if (userUpdate.getUserNickname() != null && !userUpdate.getUserNickname().equals(user.getUserNickname())) {
            if (userRepository.existsUserByUserNickname(userUpdate.getUserNickname().trim())) {
                throw new RuntimeException("이미 존재하는 닉네임입니다.");
            }
        }
        
        // 사용자 정보 업데이트
        user.updateUserInfo(
                userUpdate.getUserName(),
                userUpdate.getUserPassword(),
                userUpdate.getUserNickname(),
                userUpdate.getUserEmail(),
                passwordEncoder
        );
        
        userRepository.save(user);
        
        return ResponseEntity.ok().build();
    }

    // @DeleteMapping("/{userId}")
    // public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
    //     userService.deleteUser(userId);
    //     return ResponseEntity.noContent().build();
    // }
}