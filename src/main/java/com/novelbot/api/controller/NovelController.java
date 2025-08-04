package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.novel.NovelCreateRequest;
import com.novelbot.api.dto.novel.NovelDto;
import com.novelbot.api.service.novel.NovelService;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/novels")
public class NovelController {

   
    private final NovelService novelService;

    public NovelController(NovelService novelService) {
        this.novelService = novelService;
    }

    @Operation(summary = "웹소설 목록 조회", description = "모든 웹소설 목록을 조회하는 API")
    @GetMapping
    public ResponseEntity<List<NovelDto>> getNovelList() {
        List<NovelDto> novels = novelService.findAllNovels();
        return ResponseEntity.ok(novels);
    }

    // @PostMapping
    // public ResponseEntity<Void> registerNovel(@RequestBody NovelCreateRequest novel) {
    //     novelService.createNovel(novel);
    //     return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created 응답
    // }

    @Operation(summary = "웹소설 상세 조회", description = "특정 웹소설의 상세 정보를 조회하는 API")
    @GetMapping("/search")
    public ResponseEntity<NovelDto> searchNovels(@RequestParam String title) {
        NovelDto searchResult = novelService.searchNovel(title);
        return ResponseEntity.ok(searchResult); //List 반환 필요
    } 

    // @DeleteMapping("/{novelId}")
    // public ResponseEntity<Void> deleteNovel(@PathVariable Integer novelId) {
    //     novelService.deleteNovel(novelId);
    //     return ResponseEntity.noContent().build();
    // }

    // @PatchMapping("/{novelId}")
    // public ResponseEntity<Void> updateNovel(@PathVariable Integer novelId, @RequestBody NovelCreateRequest novel) {
    //     novelService.updateNovel(novelId, novel);
    //     return ResponseEntity.ok().build();
    // }
}