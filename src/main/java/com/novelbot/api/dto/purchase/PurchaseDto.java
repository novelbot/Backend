package com.novelbot.api.dto.purchase;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseDto {
    private int episodeId;
    private int novelId;
    private int userId;
    private Boolean isPurchase;
}
