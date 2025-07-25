package com.novelbot.api.repository;

import com.novelbot.api.domain.Novel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, int>{
    Optional<Novel> findByTitle(String title);
}