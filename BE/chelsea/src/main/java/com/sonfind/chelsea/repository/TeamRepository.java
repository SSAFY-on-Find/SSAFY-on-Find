package com.sonfind.chelsea.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sonfind.chelsea.domain.teams.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
	@Query("SELECT t from Team t WHERE t.teamId =:teamId AND t.isDeleted=false")
	Optional<Team> findTeamByTeamId(@Param("teamId") Long teamId);

	List<Team> findByIsDeletedIsFalseOrderByTeamIdAsc();
}
