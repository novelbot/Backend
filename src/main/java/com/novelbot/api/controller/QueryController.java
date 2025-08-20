package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.chat.QueryCreateRequest;
import com.novelbot.api.dto.chat.QueryDto;
import com.novelbot.api.service.chat.QueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@RestController
@RequestMapping("/chatrooms/{chatId}")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @Operation(summary = "질문 생성", description = "새로운 질문을 생성하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "생성된 질문 ID 반환",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class, example = "123"))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음")
    })
    @PostMapping("/queries")
    public ResponseEntity<?> createQuery(@PathVariable String chatId,
            @RequestBody QueryCreateRequest request,
            @RequestHeader("Authorization") String token) {
        try {
            // chatId 유효성 검증
            if ("undefined".equals(chatId) || "null".equals(chatId)) {
                return ResponseEntity.badRequest().body("채팅방 ID가 올바르지 않습니다. chatId: " + chatId);
            }
            
            Integer parsedChatId = Integer.parseInt(chatId);
            Integer queryId = queryService.createQueryAsync(parsedChatId, request.getQueryContent(), token);
            return ResponseEntity.status(HttpStatus.CREATED).body(queryId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("채팅방 ID는 숫자여야 합니다. chatId: " + chatId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("질문 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Operation(summary = "질문 목록 조회", description = "채팅방의 모든 질문을 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음")
    })
    @GetMapping("/queries")
    public ResponseEntity<?> getQueries(@PathVariable String chatId,
            @RequestHeader("Authorization") String token) {
        try {
            // chatId 유효성 검증
            if ("undefined".equals(chatId) || "null".equals(chatId)) {
                return ResponseEntity.badRequest().body("채팅방 ID가 올바르지 않습니다. chatId: " + chatId);
            }
            
            Integer parsedChatId = Integer.parseInt(chatId);
            List<QueryDto> queries = queryService.getQueriesByChatId(parsedChatId, token);
            return ResponseEntity.ok(queries);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("채팅방 ID는 숫자여야 합니다. chatId: " + chatId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("질문 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Operation(summary = "질문 삭제", description = "특정 질문을 삭제하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "질문 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "질문을 찾을 수 없음")
    })
    @DeleteMapping("/queries/{queryId}")
    public ResponseEntity<Void> deleteQuery(@PathVariable Integer queryId) {
        queryService.deleteQuery(queryId);
        return ResponseEntity.noContent().build();
    }

}