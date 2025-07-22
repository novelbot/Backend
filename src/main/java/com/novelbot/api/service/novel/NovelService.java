package com.novelbot.api.service.novel;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.repository.NovelRepository;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NovelService {
    private final NovelRepository novelRepository;

    public NovelService(NovelRepository novelRepository) {
        this.novelRepository = novelRepository;
    }

    public List<Novel> getNovelList() {
        return novelRepository.findAll();
    }
}
