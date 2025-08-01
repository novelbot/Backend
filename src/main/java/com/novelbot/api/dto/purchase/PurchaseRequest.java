package com.novelbot.api.dto.purchase;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseRequest {
    private Integer episodeId;
    private Integer novelId;
}
