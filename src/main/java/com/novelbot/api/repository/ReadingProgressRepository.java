package com.novelbot.api.repository;

import com.novelbot.api.domain.UserReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingProgressRepository extends JpaRepository<UserReadingProgress, Long> {
    
}
