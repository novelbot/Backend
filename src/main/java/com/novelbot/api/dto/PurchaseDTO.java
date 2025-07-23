package com.novelbot.dto;

import lombok.Data;

@Data
public class PurchaseDTO {
    private Long episode_id;
    private Long novel_id;
    private Long user_id;
    private Boolean is_purchase;
}
