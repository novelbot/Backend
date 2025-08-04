package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.chat.QueryCreateRequest;
import com.novelbot.api.dto.chat.QueryDto;
import com.novelbot.api.service.chat.QueryService;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;



@RestController
@RequestMapping("/chatrooms/{chatId}")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @Operation(summary = "질문 생성", description = "새로운 질문을 생성하는 API")
    @PostMapping("/queries")
    public ResponseEntity<Void> createQuery(@PathVariable Integer chatId, @RequestBody QueryCreateRequest request) {
        request.setChatId(chatId);
        queryService.createQuery(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/queries")
    public ResponseEntity<List<QueryDto>> getQueries(@PathVariable Integer chatId) {
        List<QueryDto> queries = queryService.getQueriesByChatId(chatId);
        return ResponseEntity.ok(queries);
    }

    @Operation(summary = "질문 삭제", description = "특정 질문을 삭제하는 API")
    @DeleteMapping("/queries/{queryId}")
    public ResponseEntity<Void> deleteQuery(@PathVariable Integer queryId) {
        queryService.deleteQuery(queryId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "답변 생성", description = "질문에 대한 답변을 생성하는 API")
    @GetMapping("/queries/{queryId}/answer")
    public ResponseEntity<QueryDto> getAnswer(@PathVariable Integer queryId) {
        QueryDto response = queryService.generateAnswer(queryId);
        return ResponseEntity.ok(response);
    }
}