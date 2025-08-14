package com.novelbot.api.controller;

import com.novelbot.api.service.reading.ReadingProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.reading.ReadingProgressDto;
import com.novelbot.api.dto.reading.ReadingProgressRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/reading-progress")
public class ReadingProgressController {

    private ReadingProcessService readingProcessService;

    //새로운 독서 진도 저장
    @Operation(summary = "독서 진도 저장", description = "새로운 독서 진도를 저장하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "독서 진도 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 - 유효하지 않은 소설 또는 에피소드 ID"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자, 소설 또는 에피소드를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public ResponseEntity<Void> saveReadingProgress(@RequestBody ReadingProgressRequest request,
                                                    @RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        readingProcessService.initializeReading(request, jwtToken);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //독서 진도 업데이트
    @Operation(summary = "독서 진도 업데이트", description = "독서 진도를 업데이트하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "독서 진도 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "독서 진도를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PatchMapping
    public ResponseEntity<Void> updateReadingProgress(@RequestBody ReadingProgressRequest request,
                                                      @RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        readingProcessService.updateProgress(request, jwtToken);
        return ResponseEntity.ok().build();
    }

    //읽던 페이지 이동
    @Operation(summary = "읽던 페이지 이동", description = "읽던 페이지를 이동하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "마지막 읽은 위치로 리다이렉트"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "독서 진도를 찾을 수 없음")
    })
    @GetMapping
    public ResponseEntity<Integer> getReadingProgress(@RequestBody ReadingProgressDto request,
                                                      @RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        ReadingProgressDto readingProgressDto = new ReadingProgressDto();
        readingProgressDto.setNovelId(request.getNovelId());
        readingProgressDto.setEpisodeId(request.getEpisodeId());

        int lastReadPage = readingProcessService.getProgress(readingProgressDto, jwtToken);
        return ResponseEntity.ok(lastReadPage);
    }
}