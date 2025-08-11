package com.sonfind.chelsea.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.teams.Recruitment;
import com.sonfind.chelsea.global.domain.SubCode;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

	@Query("SELECT r.position FROM Recruitment r WHERE r.team.teamId = :teamId")
	List<SubCode> findPositionByTeamId(Long teamId);
}
