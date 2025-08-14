package com.novelbot.api.service.join;

import com.novelbot.api.domain.User;
import com.novelbot.api.dto.join.UserCreateRequest;
import com.novelbot.api.mapper.join.UserCreateRequestMapper;
import com.novelbot.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegistrationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCreateRequestMapper userCreateRequestMapper;

    public void registerUser(UserCreateRequest userCreateRequest) {
        if(userCreateRequest == null || userCreateRequest.getUserName() == null || userCreateRequest.getUserName().trim().isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(사용자 이름이 비어있습니다.)"
            );
        }
        if(userCreateRequest.getUserEmail().trim().isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(이메일이 비어있습니다)"
            );
        }
        if(userCreateRequest.getUserPassword().trim().isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(비밀번호가 비어 있습니다)"
            );
        }
        if(userRepository.existsUserByUserName(userCreateRequest.getUserName().trim())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Error Code: 409, Conflict(이미 사용 중인 사용자 이름입니다)"
            );
        }
        if(userRepository.existsUsersByUserEmail(userCreateRequest.getUserEmail().trim())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Error Code: 409, Conflict(이미 가입된 이메일입니다)"
            );
        }
        if(userCreateRequest.getUserNickname() != null && !userCreateRequest.getUserNickname().trim().isEmpty() 
           && userRepository.existsUserByUserNickname(userCreateRequest.getUserNickname().trim())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Error Code: 409, Conflict(이미 사용 중인 닉네임입니다)"
            );
        }

        User user = userCreateRequestMapper.toUser(userCreateRequest);

        try{
            userRepository.save(user);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error Code: 500, Internal Server Error(사용자 저장 중 오류 발생)"
            );
        }
    }

}
