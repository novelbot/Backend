package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.chat.ChatroomCreateRequest;
import com.novelbot.api.service.chat.ChatroomService;

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
    @PostMapping("/chatrooms")
    public ResponseEntity<Void> createChatroom(@RequestBody ChatroomCreateRequest request, String token) {
        chatroomService.createChatroom(request, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/chatrooms/{chatId}")
    public ResponseEntity<Void> deleteChatroom(@PathVariable Integer chatId) {
        chatroomService.deleteChatroom(chatId);
        return ResponseEntity.noContent().build();
    }
}