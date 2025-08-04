package com.novelbot.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.novelbot.api.dto.purchase.PurchaseDto;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Operation(summary = "웹소설 에피소드 구매", description = "특정 웹소설의 에피소드를 구매하는 API")
    @PostMapping
    public ResponseEntity<Void> purchaseEpisode(@RequestBody PurchaseDto purchaseRequest) {
        // TODO: 에피소드 구매 처리 비즈니스 로직 (Service 호출)
        // 예: purchaseService.createPurchase(purchaseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "구매 목록 조회", description = "현재 사용자의 구매 목록을 조회하는 API")
    @GetMapping
    public ResponseEntity<List<PurchaseDto>> getPurchaseList() {
        // TODO: 현재 사용자의 구매 목록 조회 로직 (Service 호출)
        // 예: List<Purchase> purchases = purchaseService.getPurchaseHistory(userId);
        // return ResponseEntity.ok(purchases);
        return ResponseEntity.ok().build(); // 임시 반환
    }
}