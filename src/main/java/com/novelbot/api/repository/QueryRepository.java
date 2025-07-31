package com.novelbot.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.novelbot.api.domain.Queries;

public interface QueryRepository extends JpaRepository<Queries, Integer> {
}