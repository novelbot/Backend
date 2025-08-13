package com.novelbot.api.repository;

import com.novelbot.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserByUserName(String trim);

    boolean existsUsersByUserEmail(String trim);

    Optional<User> findByUserName(String userName);

    Optional<User> findByIdAndUserName(Integer id, String userName);
}
