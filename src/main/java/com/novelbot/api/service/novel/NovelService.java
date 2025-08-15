package com.novelbot.api.service.novel;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.dto.novel.NovelCreateRequest;
import com.novelbot.api.dto.novel.NovelDto;
import com.novelbot.api.mapper.novel.NovelDtoMapper;
import com.novelbot.api.service.GCS.GCSService;
import com.novelbot.api.repository.NovelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NovelService {
    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private NovelDtoMapper novelDtoMapper;

    @Autowired
    private GCSService gcsService;

    // 소설 목록 조회
    public List<NovelDto> findAllNovels() {

        List<Novel> novelList = novelRepository.findAll();
        
        return novelList.stream()
                .map(novel -> {
                    try {
                        return novelDtoMapper.toDto(novel);
                    } catch (Exception e) {
                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Error Code: 409, Conflict(소설 매핑 중 충돌 발생: " + e.getMessage() + ")"
                        );
                    }
                })
                .collect(Collectors.toList());
    }

    //소설 id 탐색
    public NovelDto findById(int novelId) {
        if (novelId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Error Code: 400, Bad Request(유효하지 않은 novelId)"
            );
        }
        
        Optional<Novel> optionalNovel = novelRepository.findById(novelId);

        if (optionalNovel.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Error Code: 404, Conflict(존재하지 않는 novelId)"
            );
        }

        try {
            return novelDtoMapper.toDto(optionalNovel.get());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Error Code: 409, Conflict(소설 매핑 중 충돌 발생: " + e.getMessage() + ")"
            );
        }
    }

    // 소설 제목 검색
    public NovelDto searchNovel(String novelTitle) {
        if (novelTitle == null || novelTitle.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Error Code: 400, Bad Request(소설 제목을 올바르게 입력해주세요)"
            );
        }

        Optional<Novel> optionalNovel = novelRepository.findByTitle(novelTitle.trim());

        if (optionalNovel.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Error Code: 404, Not Found(해당 소설 제목을 찾을 수 없습니다: " + novelTitle + ")"
            );
        }

        try {
            return novelDtoMapper.toDto(optionalNovel.get());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Error Code: 409, Conflict(소설 매핑 중 충돌 발생: " + e.getMessage() + ")"
            );
        }
    }

    // 웹소설 등록
    public void createNovel(NovelCreateRequest novelCreateRequest) {
        if(novelCreateRequest.getTitle() == null || novelCreateRequest.getTitle().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "소설 제목은 필수값입니다.");
        }
        if(novelCreateRequest.getAuthor() == null || novelCreateRequest.getAuthor().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작가는 필수값입니다.");
        }

        Optional<Novel> existingNovel = novelRepository.findByTitle(novelCreateRequest.getTitle());
        if (existingNovel.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 소설입니다.");
        }

        Novel novel = new Novel();
        novel.setTitle(novelCreateRequest.getTitle());
        novel.setAuthor(novelCreateRequest.getAuthor());
        novel.setDescription(novelCreateRequest.getDescription());

        try{
            novelRepository.save(novel);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "소설 등록 중 오류가 발생했습니다.", e);
        }
    }

    // 웹소설 삭제
    public void deleteNovel(int novelId){
        if (novelId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 novelId 입니다.");
        }

        Optional<Novel> optionalNovel = novelRepository.findById(novelId);
        if (optionalNovel.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 소설입니다.");
        }

        try{
            novelRepository.delete(optionalNovel.get());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "소설 삭제 중 오류가 발생했습니다.", e);
        }
    }

    // 웹소설 수정
    public void updateNovel(NovelDto novelDto) {
        if(novelDto.getNovelId() == null || novelDto.getNovelId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 novelId입니다.");
        }
        if(novelDto.getTitle() == null || novelDto.getTitle().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "소설 제목은 필수값입니다.");
        }
        if(novelDto.getAuthor() == null || novelDto.getAuthor().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"작가명은 필수값입니다.");
        }

        Optional<Novel> optionalNovel = novelRepository.findById(novelDto.getNovelId());
        if (optionalNovel.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않은 소설입니다.");
        }

        Novel novel = optionalNovel.get();
        novel.setTitle(novelDto.getTitle());
        novel.setAuthor(novelDto.getAuthor());
        novel.setDescription(!novelDto.getDescription().isEmpty() ? novelDto.getDescription().trim() : "");

        try{
            novelRepository.save(novel);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "소설 수정 중 오류가 발생했습니다");
        }
    }

    // 표지 이미지 업로드
    public Novel uploadCoverImage(int novelId, MultipartFile file) throws IOException {
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new IllegalStateException("소설이 존재하지 않습니다."));

        String image_url = gcsService.uploadFile(file);

        novel.setCoverImageUrl(image_url);

        return novelRepository.save(novel);
    }

    // 표지 이미지 URL 조회
    public String getCoverImageUrl(int novelId) {
        if(novelId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "novelId값이 올바르지 않습니다.");
        }

        Optional<Novel> optionalNovel = novelRepository.findById(novelId);
        if (optionalNovel.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "소설이 존재하지 않습니다");
        }

        String coverImageUrl = optionalNovel.get().getCoverImageUrl();

        if(coverImageUrl == null || coverImageUrl.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 소설의 표지 이미지가 존재하지 않습니다.");
        }

        return coverImageUrl;
    }
}