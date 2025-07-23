package com.novelbot.repository;

import com.novelbot.entity.Novel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NovelRepository extends JpaRepository<Novel, Long>{
}
