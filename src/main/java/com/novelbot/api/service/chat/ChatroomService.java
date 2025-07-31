package com.novelbot.api.service.chat;

import com.novelbot.api.domain.Chatroom;
import com.novelbot.api.domain.Novel;
import com.novelbot.api.domain.User;
import com.novelbot.api.dto.chat.ChatroomCreateRequest;
import com.novelbot.api.mapper.chat.ChatroomCreateRequestDtoMapper;
import com.novelbot.api.repository.ChatRepository;
import com.novelbot.api.repository.NovelRepository;
import com.novelbot.api.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ChatroomService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatroomCreateRequestDtoMapper chatroomCreateRequestDtoMapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    // 채팅방 생성
    public void createChatroom(ChatroomCreateRequest chatroomCreateRequest, String token) {
        if(chatroomCreateRequest == null || chatroomCreateRequest.getNovelId() <= 0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(novelId가 올바르지 않는 형식입니다.)"
            );
        }
        if (chatroomCreateRequest.getChatTitle() == null || chatroomCreateRequest.getChatTitle().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(채팅방 제목이 비어 있습니다)"
            );
        }
        if (token == null || token.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(토큰이 비어 있습니다)"
            );
        }

        String username;
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            username = claims.getSubject();
        }catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Error Code: 401, Unauthorized(유효하지 않은 토큰입니다)"
            );
        }

        Optional<User> user = Optional.ofNullable(userRepository.findByUserName(username).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Error Code: 404, Not Found(사용자가 존재하지 않습니다.)"
                )));
        Optional<Novel> novel = Optional.ofNullable(novelRepository.findById(chatroomCreateRequest.getNovelId()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Error Code: 404, Not Found(소설을 찾을 수 없습니다)"
                )));

        // user_name 파싱하는 로직 필요 -> user_name 값으로 해당하는 user 객체 찾는 로직 추가..
        // userRepository.findById(userId).ifPresent(user -> {}) 어쩌구.. 저쩌구..

        // novel_id 값으로 해당하는 소설 객체 찾는 로직 추가..
        // novelRepository.findById(novelId).ifPresent(novel -> {}) 어쩌구.. 저쩌구..

        // Chatroom chatroom = new Chatroom(); // 기본 생성자.

        Chatroom chatroom = new Chatroom(
            chatroomCreateRequest.getChatTitle(), user, novel
        );

        try{
            chatRepository.save(chatroom);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error Code: 500, Internal Server Error(채팅방 생성 중 오류 발생)"
            );
        }
    }

    // 채팅방 조회
    public List<Chatroom> getAllChatrooms() {
        return chatRepository.findAll();
    }

      // 채팅방 token으로 조회하기..?
//    public List<Chatroom> getAllChatroom(String token){
//        if(token == null || token.trim().isEmpty()){
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(유효하지 않은 토큰 형태입니다)"
//            );
//        }
//        return chatRepository.findByToken(token);
//    }

    // 채팅방 삭제
    public void deleteChatroom(int chatId) {
        try {
            chatRepository.deleteById(chatId);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error Code: 500, Internal Server Error(채팅방 삭제 중 오류 발생: " + e.getMessage() + ")"
            );
        }
    }
}
