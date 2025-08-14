package com.novelbot.api.service.join;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.novelbot.api.config.JwtTokenValidator;
import com.novelbot.api.domain.User;
import com.novelbot.api.dto.user.UserResponseDto;
import com.novelbot.api.dto.user.UserUpdateDto;
import com.novelbot.api.repository.UserRepository;

@Service
public class UserService {

    private final JwtTokenValidator jwtTokenValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(JwtTokenValidator jwtTokenValidator, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtTokenValidator = jwtTokenValidator;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto getUserInfo(String token) {
        Integer userId = jwtTokenValidator.getUserId(token);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        return new UserResponseDto(
                user.getUserName(),
                user.getUserNickname(),
                user.getUserEmail()
        );
    }

    public void updateUserInfo(String token, UserUpdateDto userUpdate) {
        Integer userId = jwtTokenValidator.getUserId(token);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 사용자명 중복 체크 (현재 사용자가 아닌 경우)
        if (userUpdate.getUserName() != null && !userUpdate.getUserName().equals(user.getUserName())) {
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
    }
}