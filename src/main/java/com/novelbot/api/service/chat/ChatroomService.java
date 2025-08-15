package com.novelbot.api.service.chat;

import com.novelbot.api.domain.Chatroom;
import com.novelbot.api.domain.Novel;
import com.novelbot.api.domain.User;
import com.novelbot.api.dto.chat.ChatroomDto;
import com.novelbot.api.dto.novel.NovelListDto;
import com.novelbot.api.mapper.chat.ChatroomDtoMapper;
import com.novelbot.api.config.JwtTokenValidator;
import com.novelbot.api.repository.ChatRepository;
import com.novelbot.api.repository.NovelRepository;
import com.novelbot.api.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        System.out.println("DEBUG: createChatroom called with novelId=" + novelId + ", chatTitle=" + chatTitle + ", token=" + (token != null ? "present" : "null"));
        
        if (novelId == null || novelId <= 0) {
            System.out.println("DEBUG: Invalid novelId: " + novelId);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "novelId가 올바르지 않은 형식입니다.");
        }
        if (chatTitle == null || chatTitle.isEmpty()) {
            System.out.println("DEBUG: Invalid chatTitle: " + chatTitle);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "채팅방 제목이 비어 있습니다.");
        }
        if (token == null || token.trim().isEmpty()) {
            System.out.println("DEBUG: Invalid token");
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "토큰이 비어 있습니다.");
        }

        Integer userId = jwtTokenValidator.getUserId(token);

        User user = userRepository.findById(userId).orElseThrow(
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
        Integer userId = jwtTokenValidator.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));

        List<Chatroom> chatrooms = chatRepository.findByUser(user);
        return chatrooms.stream()
                .map(chatroomDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    // 채팅방 삭제 (소유자 확인)
    public void deleteChatroom(Integer chatId, String token) {
        Integer userIdFromToken = jwtTokenValidator.getUserId(token);

        Chatroom chatroom = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID가 " + chatId + "인 채팅방을 찾을 수 없습니다."));

        // chatroom의 소유자 ID와 토큰의 사용자 ID를 비교합니다.
        if (!chatroom.getUser().getId().equals(userIdFromToken)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 채팅방을 삭제할 권한이 없습니다.");
        }
        try {
            chatRepository.delete(chatroom);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 삭제 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional(readOnly = true)
    public List<NovelListDto> getChatRoomNovelList(String token) {
        Integer userId = jwtTokenValidator.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));

        List<Chatroom> chatrooms = chatRepository.findByUser(user);
        return chatrooms.stream()
                .map(chatroom -> chatroom.getNovel())
                .distinct()
                .map(novel -> new NovelListDto(novel.getId(), novel.getTitle()))
                .collect(Collectors.toList());
    }

    public List<ChatroomDto> getChatroomsByNovel(Integer novelId, String token) {
        if (novelId == null || novelId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "novelId가 올바르지 않은 형식입니다.");
        }
        
        Integer userId = jwtTokenValidator.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));
        
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소설을 찾을 수 없습니다."));

        List<Chatroom> chatrooms = chatRepository.findByUserAndNovel(user, novel);
        return chatrooms.stream()
                .map(chatroomDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
