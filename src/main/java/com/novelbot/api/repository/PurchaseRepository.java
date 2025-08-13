package com.novelbot.api.repository;

import com.novelbot.api.domain.Episode;
import com.novelbot.api.domain.Novel;
import com.novelbot.api.domain.Purchase;
import com.novelbot.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    List<Purchase> findByUserAndEpisodeAndNovel(User user, Episode episode, Novel novel);

    List<Purchase> findByUserAndNovel(User user, Novel novel);

    List<Purchase> findByUser(User user);
}
