package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.novel.NovelCreateRequest;
import com.novelbot.api.dto.novel.NovelDto;
import com.novelbot.api.service.novel.NovelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/novels")
public class NovelController {

   
    private final NovelService novelService;

    public NovelController(NovelService novelService) {
        this.novelService = novelService;
    }

    @Operation(summary = "웹소설 목록 조회", description = "모든 웹소설 목록을 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹소설 목록 조회 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<List<NovelDto>> getNovelList() {
        List<NovelDto> novels = novelService.findAllNovels();
        return ResponseEntity.ok(novels);
    }

    @Operation(summary = "id를 통한 웹소설 조회", description = "특정 웹소설의 상세 정보를 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹소설 조회 성공"),
            @ApiResponse(responseCode = "404", description = "웹소설을 찾을 수 없음")
    })
    @GetMapping("/{novelId}")
    public ResponseEntity<NovelDto> getNovel(@PathVariable Integer novelId){
        NovelDto novel = novelService.findById(novelId);
        return ResponseEntity.ok(novel);
    }

     @PostMapping
     public ResponseEntity<Void> registerNovel(@RequestBody NovelCreateRequest novel) {
         novelService.createNovel(novel);
         return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created 응답
     }

    @Operation(summary = "웹소설 검색", description = "제목으로 웹소설을 검색하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "웹소설 검색 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 - 제목이 비어있음"),
            @ApiResponse(responseCode = "404", description = "검색 결과 없음")
    })
    @GetMapping("/search")
    public ResponseEntity<NovelDto> searchNovels(@RequestParam String title) {
        NovelDto searchResult = novelService.searchNovel(title);
        return ResponseEntity.ok(searchResult); //List 반환 필요
    } 

     @DeleteMapping("/{novelId}")
     public ResponseEntity<Void> deleteNovel(@PathVariable Integer novelId) {
         novelService.deleteNovel(novelId);
         return ResponseEntity.noContent().build();
     }

     @PatchMapping("/{novelId}")
     public ResponseEntity<Void> updateNovel(@RequestBody NovelDto novel) {
         novelService.updateNovel(novel);
         return ResponseEntity.ok().build();
     }
}