package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.chat.AnswerResponse;
import com.novelbot.api.dto.chat.QueryCreateRequest;
import com.novelbot.api.service.chat.QueryService;

import io.swagger.v3.oas.annotations.Operation;



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
        queryService.createQuery(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // @GetMapping("/queries")
    // public ResponseEntity<List<QueryDto>> getQueries(@PathVariable Integer chatId) {
    //     TODO: 질문 목록 조회 로직
    //     서비스 로직을 통해 `chatId`에 해당하는 질문 목록을 가져옵니다.
    //     아래 코드는 비어있는 리스트를 JSON 배열 '[]'로 변환하여 반환합니다.
    //     return ResponseEntity.ok(Collections.emptyList());
    // }

    @Operation(summary = "질문 삭제", description = "특정 질문을 삭제하는 API")
    @DeleteMapping("/queries/{queryId}")
    public ResponseEntity<Void> deleteQuery(@PathVariable Integer queryId) {
        // TODO: 쿼리 삭제 로직
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "답변 생성", description = "질문에 대한 답변을 생성하는 API")
    @GetMapping("/queries/{queryId}/answer")
    public ResponseEntity<AnswerResponse> getAnswer(@PathVariable Integer queryId) {
        // TODO: 답변 생성 로직
        // 실제로는 서비스 계층을 통해 `queryId`에 해당하는 답변 데이터를 조회하고,
        // `AnswerResponse` 객체를 생성하여 반환해야 합니다.
        // 예: return ResponseEntity.ok(new AnswerResponse(...));
        return ResponseEntity.ok().build(); // 현재는 비어있는 Body를 반환합니다.
    }
}