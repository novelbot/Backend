package com.novelbot.api.service.chat;

import com.novelbot.api.domain.Chatroom;
import com.novelbot.api.domain.Novel;
import com.novelbot.api.domain.User;
import com.novelbot.api.dto.chat.ChatroomDto;
import com.novelbot.api.mapper.chat.ChatroomDtoMapper;
import com.novelbot.api.config.JwtTokenValidator;
import com.novelbot.api.repository.ChatRepository;
import com.novelbot.api.repository.NovelRepository;
import com.novelbot.api.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatroomService {
    private final ChatRepository chatRepository;
    private final NovelRepository novelRepository;
    private final UserRepository userRepository;
    private final ChatroomDtoMapper chatroomDtoMapper;
    private final JwtTokenValidator jwtTokenValidator;

    public ChatroomService(ChatRepository chatRepository,
            NovelRepository novelRepository,
            UserRepository userRepository,
            ChatroomDtoMapper chatroomDtoMapper,
            JwtTokenValidator jwtTokenValidator) {
        this.chatRepository = chatRepository;
        this.novelRepository = novelRepository;
        this.userRepository = userRepository;
        this.chatroomDtoMapper = chatroomDtoMapper;
        this.jwtTokenValidator = jwtTokenValidator;
    }

    // 채팅방 생성
    public void createChatroom(Integer novelId, String chatTitle, String token) {
        if (novelId == null || novelId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "novelId가 올바르지 않은 형식입니다.");
        }
        if (chatTitle == null || chatTitle.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "채팅방 제목이 비어 있습니다.");
        }
        if (token == null || token.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "토큰이 비어 있습니다.");
        }

        String username = jwtTokenValidator.getUsername(token);

        User user = userRepository.findByUserName(username).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        Novel novel = novelRepository.findById(novelId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "소설을 찾을 수 없습니다."));

        Chatroom chatroom = new Chatroom(chatTitle, user, novel);

        try {
            chatRepository.save(chatroom);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 생성 중 오류가 발생했습니다.", e);
        }
    }

    // 사용자의 채팅방 목록 조회
    public List<ChatroomDto> getChatroomsByUser(String token) {
        String username = jwtTokenValidator.getUsername(token);
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        List<Chatroom> chatrooms = chatRepository.findByUser(user);
        return chatrooms.stream()
                .map(chatroomDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    // 채팅방 삭제 (소유자 확인)
    public void deleteChatroom(Integer chatId, String token) {
        String usernameFromToken = jwtTokenValidator.getUsername(token);

        Chatroom chatroom = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID가 " + chatId + "인 채팅방을 찾을 수 없습니다."));

        if (!chatroom.getUser().getUserName().equals(usernameFromToken)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 채팅방을 삭제할 권한이 없습니다.");
        }
        try {
            chatRepository.delete(chatroom);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 삭제 중 오류가 발생했습니다.", e);
        }
    }
}
