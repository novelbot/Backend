package com.novelbot.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QueryRepository extends JpaRepository<Query, Integer> {
}