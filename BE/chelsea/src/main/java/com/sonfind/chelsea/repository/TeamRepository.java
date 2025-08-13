package com.sonfind.chelsea.repository;

import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.teams.TeamMemberDto;
import com.sonfind.chelsea.dto.teams.TeamMini;
import com.sonfind.chelsea.dto.teams.TeamRecruitmentDto;
import com.sonfind.chelsea.dto.teams.TeamWithMembersDto;
import com.sonfind.chelsea.global.domain.SubCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
	@Query("SELECT t from Team t WHERE t.teamId =:teamId AND t.isDeleted=false")
	Optional<Team> findTeamByTeamId(@Param("teamId") Long teamId);

	List<Team> findByIsDeletedIsFalseOrderByTeamIdAsc();

	//fetch join
	@Query("""
				SELECT DISTINCT t FROM Team t
				LEFT JOIN FETCH t.track
				LEFT JOIN FETCH t.recruitments r
				LEFT JOIN FETCH r.position
				WHERE t.isDeleted = false
				ORDER BY t.teamId ASC
			""")
	List<Team> findAllTeamsWithRecruitments();

	//Dto 프로젝션
	@Query("""
				SELECT new com.sonfind.chelsea.dto.teams.TeamWithMembersDto(
					t.teamId, t.name, t.description,
					t.track.subCode, t.track.subCodeName,
					t.majorCount, t.nonMajorCount
					)
					FROM Team t
					LEFT JOIN t.track
					WHERE t.isDeleted = false
					ORDER BY t.teamId ASC
			""")
	List<TeamWithMembersDto> findAllTeamSummaries();

	//팀 별 멤버 별도로 조회
	@Query("""
				SELECT new com.sonfind.chelsea.dto.teams.TeamMemberDto(
					s.teamId, s.studentId, s.name, s.majorYn,
					si.profile.profileImageUrl,
					pos.subCode, pos.subCodeName
					)	
					FROM Student s
					LEFT JOIN StudentInfo si ON si.student.studentId = s.studentId
					LEFT JOIN si.positionCode pos
					WHERE s.teamId IN :teamIds
					ORDER BY s.teamId, s.name
			""")
	List<TeamMemberDto> findTeamMembersByTeamIds(@Param("teamIds") List<Long> teamIds);

	//팀 별 포지션 모집 별도로 조회
	@Query("""
				SELECT new com.sonfind.chelsea.dto.teams.TeamRecruitmentDto(
					r.team.teamId,
					r.position.subCode,
					r.position.subCodeName
				)
				FROM Recruitment r
				LEFT JOIN r.position
				WHERE r.team.teamId IN :teamIds	
			""")
	List<TeamRecruitmentDto> findRecruitmentsByTeamIds(@Param("teamIds") List<Long> teamIds);

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

	@Query("""
			    select t.teamId as teamId,
			           t.name as name,
			           t.track.subCodeName as track,
			           t.majorCount as majorCount,
			           t.nonMajorCount as nonMajorCount
			    from Team t
			    where t.teamId in :ids
			""")
	List<TeamMini> findMiniByTeamIdIn(@Param("ids") Collection<Long> ids);

	SubCode track(SubCode track);
}
