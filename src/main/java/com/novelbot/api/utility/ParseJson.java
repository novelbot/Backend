package com.novelbot.api.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON 파싱 유틸리티 클래스
 */
public class ParseJson {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * AI 서버 스트림 청크에서 텍스트만 추출
     * 
     * @param chunk AI 서버에서 받은 스트림 청크
     * @return 추출된 텍스트 (메시지가 없으면 빈 문자열)
     */
    public static String extractTextFromChunk(String chunk) {
        try {
            // JSON 형태인지 확인
            if (chunk.trim().startsWith("{") && chunk.trim().endsWith("}")) {
                JsonNode jsonNode = objectMapper.readTree(chunk);
                
                // type이 "message" 또는 "content"인 경우에만 content 추출
                if (jsonNode.has("type")) {
                    String type = jsonNode.get("type").asText();
                    if ("message".equals(type) || "content".equals(type)) {
                        if (jsonNode.has("content")) {
                            return jsonNode.get("content").asText();
                        }
                    }
                }
                
                // conversation_info나 search_complete 등은 무시
                return "";
            } else {
                // JSON이 아닌 일반 텍스트는 그대로 반환
                return chunk;
            }
        } catch (Exception e) {
            // 파싱 실패 시 원본 반환
            return chunk;
        }
    }
}