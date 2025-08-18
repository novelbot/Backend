package com.novelbot.api.controller;

import com.novelbot.api.dto.purchase.PurchaseRequest;
import com.novelbot.api.service.purchase.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Operation(summary = "웹소설 에피소드 구매", description = "특정 웹소설의 에피소드를 구매하는 API", security = @SecurityRequirement(name = "Bearer Token"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "에피소드 구매 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 - 유효하지 않은 구매 정보"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "에피소드를 찾을 수 없음"),
            @ApiResponse(responseCode = "409", description = "이미 구매한 에피소드")
    })
    @PostMapping
    public ResponseEntity<Void> purchaseEpisode(@RequestBody PurchaseRequest purchaseRequest,
            @RequestHeader("Authorization") String token) {
        purchaseService.purchaseEpisode(purchaseRequest, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}