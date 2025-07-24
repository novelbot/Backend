package com.novelbot.api.dto.purchase;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequest {
    private Long episodeId;
    private Long novelId;
    private Long userId;
    private Boolean isPurchase;
}
