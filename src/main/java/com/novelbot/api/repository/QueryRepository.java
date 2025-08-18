package com.novelbot.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.novelbot.api.domain.Queries;

import java.util.List;

public interface QueryRepository extends JpaRepository<Queries, Integer> {
    List<Queries> findByChatRoomId(Integer chatId);

    Queries findByQueryId(Integer queryId);
}