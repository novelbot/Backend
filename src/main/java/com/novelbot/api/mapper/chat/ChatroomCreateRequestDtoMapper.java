package com.novelbot.api.mapper.chat;

import com.novelbot.api.domain.Chatroom;
import com.novelbot.api.dto.chat.ChatroomCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class ChatroomCreateRequestDtoMapper {
    public ChatroomCreateRequest toDto(Chatroom chatroom) {
        ChatroomCreateRequest chatroomCreateRequest = new ChatroomCreateRequest();

        chatroomCreateRequest.setNovelId(chatroom.getNovel().getNovelId());
        chatroomCreateRequest.setChatTitle(chatroom.getChatTitle());

        return chatroomCreateRequest;
    }
}
