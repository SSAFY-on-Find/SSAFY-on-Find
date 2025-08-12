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

	@Query("""
			SELECT tf FROM TeamFavorite tf
			WHERE tf.student.studentId = :studentId
			AND tf.team.teamId IN :teamIds
			AND tf.isFavorite = true
		""")
	List<TeamFavorite> findFavoritesByStudentAndTeams(
		@Param("studentId") Long studentId,
		@Param("teamIds") List<Long> teamIds
	);
}

