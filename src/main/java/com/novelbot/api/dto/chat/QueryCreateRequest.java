package com.novelbot.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryCreateRequest {
    private String queryContent;
    private int pageNumber;
}
