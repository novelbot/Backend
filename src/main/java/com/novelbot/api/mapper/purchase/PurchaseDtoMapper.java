package com.novelbot.api.mapper.purchase;

import com.novelbot.api.domain.Purchase;
import com.novelbot.api.dto.purchase.PurchaseDto;
import org.springframework.stereotype.Component;

@Component
public class PurchaseDtoMapper {
    public PurchaseDto toDto(Purchase purchase) {
        PurchaseDto purchaseDto = new PurchaseDto();

        purchaseDto.setIsPurchase(purchase.getIsPurchase());
        purchaseDto.setNovelId(purchase.getNovel().getNovelId());
        purchaseDto.setEpisodeId(purchase.getEpisode().getEpisodeId());
        purchaseDto.setUserId(purchase.getUser().getUserId());

        return purchaseDto;
    }
}
