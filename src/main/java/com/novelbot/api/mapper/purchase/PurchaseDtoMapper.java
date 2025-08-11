package com.novelbot.api.mapper.purchase;

import com.novelbot.api.domain.Purchase;
import com.novelbot.api.dto.purchase.PurchaseDto;
import org.springframework.stereotype.Component;

@Component
public class PurchaseDtoMapper {
    public PurchaseDto toDto(Purchase purchase) {
        PurchaseDto purchaseDto = new PurchaseDto();

        purchaseDto.setNovelId(purchase.getNovel().getId());
        purchaseDto.setEpisodeId(purchase.getEpisode().getId());
        purchaseDto.setUserId(purchase.getUser().getId());

        return purchaseDto;
    }
}
