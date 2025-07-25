package com.sonfind.chelsea.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonfind.chelsea.domain.teams.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
