package com.novelbot.api.repository;

import com.novelbot.api.domain.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chatroom, Integer> {
    Object save(Chatroom chatroom);
}