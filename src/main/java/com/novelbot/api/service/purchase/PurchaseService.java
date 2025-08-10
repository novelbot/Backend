package com.novelbot.api.service.purchase;

import com.novelbot.api.domain.Purchase;
import com.novelbot.api.domain.Novel;
import com.novelbot.api.domain.Episode;
import com.novelbot.api.domain.User;
import com.novelbot.api.dto.purchase.PurchaseRequest;
import com.novelbot.api.repository.*;
import com.novelbot.api.config.JwtTokenValidator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final NovelRepository novelRepository;
    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;
    private final JwtTokenValidator jwtTokenValidator;

    public PurchaseService(PurchaseRepository purchaseRepository,
                           NovelRepository novelRepository,
                           EpisodeRepository episodeRepository,
                           UserRepository userRepository,
                           JwtTokenValidator jwtTokenValidator){
        this.purchaseRepository = purchaseRepository;
        this.novelRepository = novelRepository;
        this.userRepository = userRepository;
        this.episodeRepository = episodeRepository;
        this.jwtTokenValidator = jwtTokenValidator;
    }
}
