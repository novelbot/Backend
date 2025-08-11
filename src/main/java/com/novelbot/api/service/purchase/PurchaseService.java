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

    // 에피소드 회차 구매
    public void purchaseEpisode(PurchaseRequest purchaseRequest, String token) {
        if(purchaseRequest.getNovelId() == null || purchaseRequest.getNovelId() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "novelId가 올바르지 않은 형식입니다.");
        }
        if(purchaseRequest.getEpisodeId() == null || purchaseRequest.getEpisodeId() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "episodeId가 올바르지 않은 형식입니다.");
        }

        String username = jwtTokenValidator.getUsername(token);

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        Novel novel = novelRepository.findById(purchaseRequest.getNovelId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소설을 찾을 수 없습니다."));

        Episode episode = episodeRepository.findById(purchaseRequest.getEpisodeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "에피소드를 찾을 수 없습니다."));

        boolean alreadyPurchased = purchaseRepository.existsByUserAndEpisode(user, episode);
        if(alreadyPurchased){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 구매한 에피소드입니다.");
        }

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setNovel(novel);
        purchase.setEpisode(episode);
        purchase.setIsPurchase(true);

        try{
            purchaseRepository.save(purchase);
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "구매 처리 중 오류가 발생했습니다.", e);
        }
    }

}
