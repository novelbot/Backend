package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.chat.QueryCreateRequest;
import com.novelbot.api.dto.chat.QueryDto;
import com.novelbot.api.service.chat.QueryService;

import io.swagger.v3.oas.annotations.Operation;
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
            @ApiResponse(responseCode = "201", description = "질문 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음")
    })
    @PostMapping("/queries")
    public ResponseEntity<Integer> createQuery(@PathVariable Integer chatId, Integer novelId,
                                               @RequestBody QueryCreateRequest request, String token) {
        QueryDto queryDto = queryService.createQuery(chatId, novelId, request.getQueryContent(), token);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatId);
    }

    @Operation(summary = "질문 목록 조회", description = "채팅방의 모든 질문을 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "질문 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음")
    })
    @GetMapping("/queries")
    public ResponseEntity<List<QueryDto>> getQueries(@PathVariable Integer chatId) {
        List<QueryDto> queries = queryService.getQueriesByChatId(chatId);
        return ResponseEntity.ok(queries);
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