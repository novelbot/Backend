package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.chat.ChatroomDto;
import com.novelbot.api.dto.chat.ChatroomCreateRequest;
import com.novelbot.api.dto.novel.NovelListDto;
import com.novelbot.api.service.chat.ChatroomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/chatrooms")
public class ChatController {

    private final ChatroomService chatroomService;

    public ChatController(ChatroomService chatroomService) {
        this.chatroomService = chatroomService;
    }

    @Operation(summary = "채팅방 목록 조회", description = "인증된 사용자의 채팅방 목록을 조회하는 API.", security = @SecurityRequirement(name = "Bearer Token"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅방 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패 - 유효하지 않은 토큰"),
            @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음")
    })
    @GetMapping
    public ResponseEntity<List<ChatroomDto>> getChatRooms(@RequestHeader("Authorization") String authorizationHeader) {
        List<ChatroomDto> chatRooms = chatroomService.getChatroomsByUser(authorizationHeader);
        return ResponseEntity.ok(chatRooms);
    }

    @Operation(summary = "채팅방 생성", description = "새로운 채팅방을 생성하는 API", security = @SecurityRequirement(name = "Bearer Token"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "채팅방 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 - 유효하지 않은 novelId 또는 채팅방 제목"),
            @ApiResponse(responseCode = "401", description = "인증 실패 - 유효하지 않은 토큰"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 소설을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "채팅방 생성 중 서버 오류")
    })
    @PostMapping
    public ResponseEntity<Void> createChatroom(@RequestBody ChatroomCreateRequest request,
            @RequestHeader("Authorization") String authorizationHeader) {
        System.out.println("DEBUG: ChatController.createChatroom called");
        System.out.println("DEBUG: Request - novelId=" + (request != null ? request.getNovelId() : "null") + 
                          ", chatTitle=" + (request != null ? request.getChatTitle() : "null"));
        System.out.println("DEBUG: Authorization header=" + (authorizationHeader != null ? "present" : "null"));
        
        chatroomService.createChatroom(request.getNovelId(), request.getChatTitle(), authorizationHeader);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "채팅방 삭제", description = "채팅방을 삭제하는 API (소유자만 가능)", security = @SecurityRequirement(name = "Bearer Token"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "채팅방 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패 - 유효하지 않은 토큰"),
            @ApiResponse(responseCode = "403", description = "권한 없음 - 채팅방 소유자가 아님"),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "채팅방 삭제 중 서버 오류")
    })
    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChatroom(@PathVariable Integer chatId,
            @RequestHeader("Authorization") String authorizationHeader) {
        chatroomService.deleteChatroom(chatId, authorizationHeader);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "채팅방 소설 목록 조회", description = "사용자가 생성한 채팅방의 소설 목록을 조회하는 API", security = @SecurityRequirement(name = "Bearer Token"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소설 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패 - 유효하지 않은 토큰"),
            @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음")
    })
    @GetMapping("/novels")
    public ResponseEntity<List<NovelListDto>> getChatNovelList(@RequestHeader("Authorization") String authorizationHeader) {
        List<NovelListDto> novelList = chatroomService.getChatRoomNovelList(authorizationHeader);
        return ResponseEntity.ok(novelList);
    }

    @Operation(summary = "특정 소설의 채팅방 목록 조회", description = "사용자가 특정 소설에 관해 생성한 채팅방 목록을 조회하는 API", security = @SecurityRequirement(name = "Bearer Token"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 소설의 채팅방 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 - 유효하지 않은 novelId"),
            @ApiResponse(responseCode = "401", description = "인증 실패 - 유효하지 않은 토큰"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 소설을 찾을 수 없음")
    })
    @GetMapping("/novel/{novelId}")
    public ResponseEntity<List<ChatroomDto>> getChatroomsByNovel(@PathVariable Integer novelId,
            @RequestHeader("Authorization") String authorizationHeader) {
        List<ChatroomDto> chatrooms = chatroomService.getChatroomsByNovel(novelId, authorizationHeader);
        return ResponseEntity.ok(chatrooms);
    }

}