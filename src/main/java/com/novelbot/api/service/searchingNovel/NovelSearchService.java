package com.novelbot.api.service.searchingNovel;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.repository.NovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NovelSearchService {
    @Autowired
    private NovelRepository novelRepository;

    public Novel searchNovel(String novel_title) {
        // 제목이 입력되지 않은 예외 처리
        if(novel_title == null || novel_title.isEmpty()){
            throw new IllegalArgumentException("소설 제목을 올바르게 입력해주세요.");
        }

        Optional<Novel> novels = novelRepository.findByTitle(novel_title.trim());

        // 존재하지 않는 소설 반환 요청 예외 처리
        if(novels.isEmpty()){
            throw new IllegalStateException("해당 소설의 제목을 찾을 수 없습니다: " + novel_title);
        }

        return novels.get();
    }
}
