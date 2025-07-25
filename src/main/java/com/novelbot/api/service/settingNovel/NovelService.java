package com.novelbot.api.service.settingNovel;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.repository.NovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NovelService {
    @Autowired
    private NovelRepository novelRepository;

    public List<Novel> findAllNovels() {
        return novelRepository.findAll();
    }
}
