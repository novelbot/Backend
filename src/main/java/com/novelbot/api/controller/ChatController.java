package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.chat.ChatroomDto;
import com.novelbot.api.dto.chat.ChatroomCreateRequest;
import com.novelbot.api.service.chat.ChatroomService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/chatrooms")
public class ChatController {

    private final ChatroomService chatroomService;

    public ChatController(ChatroomService chatroomService) {
        this.chatroomService = chatroomService;
    }

    @Operation(summary = "채팅방 목록 조회", description = "인증된 사용자의 채팅방 목록을 조회하는 API.")
    @GetMapping
    public ResponseEntity<List<ChatroomDto>> getChatRooms(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractTokenFromHeader(authorizationHeader);
        List<ChatroomDto> chatRooms = chatroomService.getChatroomsByUser(token);
        return ResponseEntity.ok(chatRooms);
    }

    @Operation(summary = "채팅방 생성", description = "새로운 채팅방을 생성하는 API")
    @PostMapping
    public ResponseEntity<Void> createChatroom(@RequestBody ChatroomCreateRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extractTokenFromHeader(authorizationHeader);
        chatroomService.createChatroom(request.getNovelId(), request.getChatTitle(), token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "채팅방 삭제", description = "채팅방을 삭제하는 API (소유자만 가능)")
    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChatroom(@PathVariable Integer chatId,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = extractTokenFromHeader(authorizationHeader);
        chatroomService.deleteChatroom(chatId, token);
        return ResponseEntity.noContent().build();
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization 헤더가 유효하지 않거나 Bearer 토큰이 없습니다.");
    }
}