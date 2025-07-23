package com.novelbot.api.service.settingNovel;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.repository.NovelRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class NovelService {
    @Autowired
    private NovelRepository novelRepository;

    public List<Novel> findAllNovels() {
        return novelRepository.findAll();
    }
}
