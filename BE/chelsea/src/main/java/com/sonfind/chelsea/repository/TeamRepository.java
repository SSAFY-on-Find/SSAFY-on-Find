package com.sonfind.chelsea.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.global.domain.SubCode;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
	@Query("SELECT t from Team t WHERE t.teamId =:teamId AND t.isDeleted=false")
	Optional<Team> findTeamByTeamId(@Param("teamId") Long teamId);

	List<Team> findByIsDeletedIsFalseOrderByTeamIdAsc();

	@Query("SELECT t.teamId "
		+ "FROM Team t "
		+ "INNER JOIN Recruitment r ON t = r.team "
		+ "WHERE (t.majorCount + t.nonMajorCount + :memberCnt) <= 6 "
		+ "AND t.isDeleted = FALSE "
		+ "AND (t.track = :trackCode OR r.position IN :positionCodes) ")
	List<Long> findCandidateTeams(SubCode trackCode, List<SubCode> positionCodes, int memberCnt);

	@Query(
		"""
			SELECT t
			FROM Team t
			LEFT JOIN Student s ON t.teamId= s.teamId
			LEFT JOIN StudentFavorite sf ON s.studentId = sf.targetStudent.studentId
						AND sf.student.studentId IN :teamMembers
						AND sf.isFavorite = TRUE
			LEFT JOIN TeamFavorite tf ON t.teamId = tf.team.teamId
						AND tf.student.studentId IN :teamMembers
						AND tf.isFavorite = TRUE
			WHERE (:teamId IS NULL OR t.teamId <> :teamId)
				AND (t.majorCount + t.nonMajorCount + :memberCnt) <= 6
			GROUP BY t.teamId
			ORDER BY 
					CASE WHEN t.teamId IN (:teamIdList) THEN 0 ELSE 1 END ASC,
					COUNT(tf.id) DESC,
					COUNT(sf.id) DESC,
					(t.majorCount + t.nonMajorCount) DESC,
					t.teamId ASC
			LIMIT 5
			""")
	List<Team> findRecommend(List<Long> teamMembers, List<Long> teamIdList, Long teamId, int memberCnt);

	SubCode track(SubCode track);
}
