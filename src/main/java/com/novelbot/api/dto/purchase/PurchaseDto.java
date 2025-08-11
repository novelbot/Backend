package com.novelbot.api.dto.purchase;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseDto {
    private Integer episodeId;
    private Integer novelId;
    private Integer userId;
}
