package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.purchase.PurchaseDto;

import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    /**
     * 웹소설 에피소드 구매
     */
    @PostMapping
    public ResponseEntity<Void> purchaseEpisode(@RequestBody PurchaseDto purchaseRequest) {
        // TODO: 에피소드 구매 처리 비즈니스 로직 (Service 호출)
        // 예: purchaseService.createPurchase(purchaseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 구매 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<PurchaseDto>> getPurchaseList() {
        // TODO: 현재 사용자의 구매 목록 조회 로직 (Service 호출)
        // 예: List<Purchase> purchases = purchaseService.getPurchaseHistory(userId);
        // return ResponseEntity.ok(purchases);
        return ResponseEntity.ok().build(); // 임시 반환
    }
}