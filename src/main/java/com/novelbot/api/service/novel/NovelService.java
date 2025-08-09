package com.novelbot.api.service.novel;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.dto.novel.NovelDto;
import com.novelbot.api.mapper.novel.NovelDtoMapper;
import com.novelbot.api.repository.NovelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NovelService {
    @Autowired
    private NovelRepository novelRepository;

    @Autowired
    private NovelDtoMapper novelDtoMapper;

    // 소설 목록 조회
    public List<NovelDto> findAllNovels() {

        List<Novel> novelList = novelRepository.findAll();

        if (novelList.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Error Code: 400, Bad Request(등록된 소설이 존재하지 않습니다)"
            );
        }

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
                    HttpStatus.BAD_REQUEST,
                    "Error Code: 400, Bad Request(해당 소설 제목을 찾을 수 없습니다: " + novelTitle + ")"
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
}