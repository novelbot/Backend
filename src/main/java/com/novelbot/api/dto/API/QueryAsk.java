package com.novelbot.api.dto.API;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
public class QueryAsk {
    @JsonProperty("message")
    private String queryContent; //사용자가 질문한 내용
    
    @JsonProperty("conversation_id")
    private String beforeQuery; //해당 채팅방의 가장 최신 Query의 field 값(domain/Queries 참조, isUsingContext가 true일 경우에만 포함)
    
    @JsonProperty("episode_ids")
    private Integer[] isBoughtEpisodes; //사용자가 해당 소설에서 구매한 episode들의 id 모음
    
    // @JsonProperty("novel_id") - AI 서버에서 지원하지 않음
    // private Integer novelId; //Query가 속한 chatRoom의 novelId
    
    @JsonProperty("use_conversation_context")
    private boolean isUsingContext; //이전의 질문의 맥락을 이어갈 것인지
}
