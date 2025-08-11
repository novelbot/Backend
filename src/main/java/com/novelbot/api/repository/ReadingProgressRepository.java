package com.novelbot.api.repository;

import com.novelbot.api.domain.Novel;
import com.novelbot.api.domain.User;
import com.novelbot.api.domain.UserReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingProgressRepository extends JpaRepository<UserReadingProgress, Long> {
    UserReadingProgress findByUserAndNovel(User user, Novel novel);
    // 필요한 쿼리 메소드 추가 (예: findByUserAndEpisode)
}
