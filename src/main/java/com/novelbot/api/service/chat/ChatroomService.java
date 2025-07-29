package com.novelbot.api.service.chat;

import com.novelbot.api.domain.Chatroom;
import com.novelbot.api.dto.chat.ChatroomCreateRequest;
import com.novelbot.api.mapper.chat.ChatroomCreateRequestDtoMapper;
import com.novelbot.api.repository.ChatRepository;
import com.novelbot.api.repository.NovelRepository;
import com.novelbot.api.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    // 채팅방 생성
    public void createChatroom(ChatroomCreateRequest chatroomCreateRequest) {
        if(chatroomCreateRequest == null || chatroomCreateRequest.getNovelId() <= 0){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Error Code: 400, Bad Request(novelId가 올바르지 않는 형식입니다.)"
            );
        }

        // user_id 파싱하는 로직 필요 -> user_id 값으로 해당하는 user 객체 찾는 로직 추가..
        // userRepository.findById(userId).ifPresent(user -> {}) 어쩌구.. 저쩌구..

        // novel_id 값으로 해당하는 소설 객체 찾는 로직 추가..
        // novelRepository.findById(novelId).ifPresent(novel -> {}) 어쩌구.. 저쩌구..

        Chatroom chatroom = new Chatroom(); // 일단 기본 생성자로 생성함.

//        Chatroom chatroom = new Chatroom(
//            chatroomCreateRequest.getChatTitle(), {여기 userId 값 추가로 필요}, {novelId에 해당하는 novel 객체 필요}
//        );

        try{
            chatRepository.save(chatroom);
        }catch(Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error Code: 500, Internal Server Error(사용자 저장 중 오류 발생)"
            );
        }
    }

    // 채팅방 삭제
    public void deleteChatroom(int chatId) {
        chatRepository.deleteById(chatId);
    }
}
