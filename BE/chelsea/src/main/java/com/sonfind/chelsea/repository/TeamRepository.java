package com.sonfind.chelsea.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.teams.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
	@Query("SELECT t from Team t WHERE t.teamId =:teamId AND t.isDeleted=false")
	Optional<Team> findTeamByTeamId(@Param("teamId") Long teamId);

	List<Team> findByIsDeletedIsFalseOrderByTeamIdAsc();

	@Query("""
		SELECT DISTINCT t FROM Team t 
		LEFT JOIN FETCH t.track 
		LEFT JOIN FETCH t.recruitments r 
		LEFT JOIN FETCH r.position 
		WHERE t.isDeleted = false 
		ORDER BY t.teamId
		""")
	List<Team> findAllTeamsWithTrackAndRecruitments();
	
}
