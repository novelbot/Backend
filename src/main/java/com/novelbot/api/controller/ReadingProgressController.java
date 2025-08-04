package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.reading.ReadingProgressDto;
import com.novelbot.api.dto.reading.ReadingProgressRequest;

import io.swagger.v3.oas.annotations.Operation;

import java.net.URI;

@RestController
@RequestMapping("/reading-progress")
public class ReadingProgressController {

    //새로운 독서 진도 저장
    @Operation(summary = "독서 진도 저장", description = "새로운 독서 진도를 저장하는 API")
    @PostMapping
    public ResponseEntity<Void> saveReadingProgress(@RequestBody ReadingProgressRequest request) {
        // TODO: 독서 진도 저장 로직
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //독서 진도 업데이트
    @Operation(summary = "독서 진도 업데이트", description = "독서 진도를 업데이트하는 API")
    @PatchMapping
    public ResponseEntity<Void> updateReadingProgress(@RequestBody ReadingProgressRequest request) {
        // TODO: 독서 진도 업데이트 로직
        return ResponseEntity.ok().build();
    }

    //읽던 페이지 이동
    @Operation(summary = "읽던 페이지 이동", description = "읽던 페이지를 이동하는 API")
    @GetMapping
    public ResponseEntity<ReadingProgressDto> getReadingProgress() {
        // TODO: 마지막 읽은 위치 정보 조회 로직
        // ReadingProgressResponse response = readingProgressService.getLastReadProgress(userId);
        // return ResponseEntity.status(HttpStatus.FOUND).body(response);
        return ResponseEntity.status(HttpStatus.FOUND).build(); // 임시 반환
    }
}