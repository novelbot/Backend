package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.chat.ChatroomCreateRequest;
import com.novelbot.api.service.chat.ChatroomService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatroomService chatroomService;

    public ChatController(ChatroomService chatroomService) {
        this.chatroomService = chatroomService;
    }

    //ChatRoomDto로 변환 필요
    // @GetMapping
    // public getChatRooms<List<ChatroomDto>> getChatRooms() {
    //     List<ChatroomDto> chatRooms = chatroomService.getAllChatrooms();
    //     return ResponseEntity.ok(chatRooms);
    // }

    // Chatroom
    @Operation(summary = "채팅방 생성", description = "새로운 채팅방을 생성하는 API")
    @PostMapping("/chatrooms")
    public ResponseEntity<Void> createChatroom(@RequestBody ChatroomCreateRequest request, String token) {
        chatroomService.createChatroom(request, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "채팅방 삭제", description = "채팅방을 삭제하는 API")
    @DeleteMapping("/chatrooms/{chatId}")
    public ResponseEntity<Void> deleteChatroom(@PathVariable Integer chatId) {
        chatroomService.deleteChatroom(chatId);
        return ResponseEntity.noContent().build();
    }
}