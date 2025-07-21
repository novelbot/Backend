package com.novelbot.api.service;

import com.novelbot.api.dto.NovelDTO;
import com.novelbot.api.repository.NovelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NovelService {
    private final NovelRepository novelRepository;

    public NovelService(NovelRepository novelRepository) {
        this.novelRepository = novelRepository;
    }

    public List<NovelDTO> getNovelList() {
        return novelRepository.findAll();
    }
}
