package com.novelbot.api.controller;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.repository.NovelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NovelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NovelRepository novelRepository;

    @BeforeEach
    void clean() {
        novelRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /api/novels - 소설 목록을 조회한다")
    void getAllNovels_returnsNovelList() throws Exception {
        // given (테스트 데이터 준비)
        novelRepository.saveAll(List.of(
                new Novel(),
                new Novel()
        ));

        // when & then (실행 및 검증)
        mockMvc.perform(get("/api/novels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("테스트 소설 1"))
                .andExpect(jsonPath("$[0].author").value("작가 1"))
                .andDo(print());
    }
}