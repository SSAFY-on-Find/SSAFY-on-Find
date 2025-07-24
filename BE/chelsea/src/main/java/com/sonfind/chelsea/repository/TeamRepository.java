package com.sonfind.chelsea.repository;

import com.sonfind.chelsea.domain.teams.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
