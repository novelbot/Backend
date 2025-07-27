package com.novelbot.api.mapper.chat;

import com.novelbot.api.domain.Chatroom;
import com.novelbot.api.dto.chat.ChatroomDto;
import org.springframework.stereotype.Component;

@Component
public class ChatroomDtoMapper {
    public ChatroomDto toDto(Chatroom chatroom){
        ChatroomDto chatroomDto = new ChatroomDto();

        chatroomDto.setChatId(chatroom.getChatId());
        chatroomDto.setUserId(chatroomDto.getUserId());
        chatroomDto.setNovelId(chatroom.getNovel().getNovelId());
        chatroomDto.setChatTitle(chatroom.getChatTitle());
        chatroomDto.setCreatedAt(chatroom.getCreatedAt().toString());

        return chatroomDto;
    }
}
