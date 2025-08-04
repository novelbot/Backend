package com.novelbot.api.repository;

import com.novelbot.api.domain.Chatroom;
import com.novelbot.api.domain.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chatroom, Integer> {
    List<Chatroom> findByUser(User user);
}