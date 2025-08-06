package com.sonfind.chelsea.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.Favorite.TeamFavorite;

@Repository
public interface TeamFavoriteRepository extends JpaRepository<TeamFavorite, Long> {
	Optional<TeamFavorite> findByStudentStudentIdAndTeamTeamId(Long studentId, Long teamId);

	@Query("SELECT tf.team.teamId FROM TeamFavorite tf WHERE tf.student.studentId = :studentId AND tf.team.teamId IN :teamIds AND tf.isFavorite = true")
	List<Long> findFavoriteTeamIdsByStudentIdAndTeamIds(
		@Param("studentId") Long studentId,
		@Param("teamIds") List<Long> teamIds
	);

	/**
	 * 기존 단일 조회 메서드 (이미 있다면 수정 필요)
	 */
	@Query("SELECT CASE WHEN COUNT(tf) > 0 THEN true ELSE false END FROM TeamFavorite tf WHERE tf.student.studentId = :studentId AND tf.team.teamId = :teamId AND tf.isFavorite = true")
	boolean existsByStudentIdAndTeamIdAndIsFavoriteTrue(
		@Param("studentId") Long studentId,
		@Param("teamId") Long teamId
	);

	// 또는 JPA 메서드 네이밍 사용
	boolean existsByStudent_StudentIdAndTeam_TeamIdAndIsFavoriteTrue(Long studentId, Long teamId);
}
