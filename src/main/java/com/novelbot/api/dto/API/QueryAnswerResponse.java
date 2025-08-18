package com.novelbot.api.dto.API;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
public class QueryAnswerResponse {
    @JsonProperty("conversation_id")
    String conversationId; // domain/Queries의 field의 값
    
    @JsonProperty("message")
    String answerContent;
    
    @JsonProperty("episodes_discussed")
    Integer[] referencedEpisodes;
}
