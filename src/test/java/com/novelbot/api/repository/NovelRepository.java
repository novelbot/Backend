package com.novelbot.api.repository;

import com.novelbot.api.domain.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelRepository extends JpaRepository<Novel, Long> {
    // 여기에 필요한 커스텀 쿼리 메소드를 선언할 수 있습니다.
}
