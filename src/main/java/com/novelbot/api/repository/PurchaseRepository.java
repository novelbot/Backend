package com.novelbot.api.repository;

import com.novelbot.api.domain.Episode;
import com.novelbot.api.domain.Purchase;
import com.novelbot.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    boolean existsByUserAndEpisode(User user, Episode episode);
}
