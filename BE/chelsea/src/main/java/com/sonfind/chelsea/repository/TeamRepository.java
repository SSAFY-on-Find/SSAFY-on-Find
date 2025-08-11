package com.sonfind.chelsea.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.teams.TeamMemberDto;
import com.sonfind.chelsea.dto.teams.TeamRecruitmentDto;
import com.sonfind.chelsea.dto.teams.TeamWithMembersDto;

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

}
