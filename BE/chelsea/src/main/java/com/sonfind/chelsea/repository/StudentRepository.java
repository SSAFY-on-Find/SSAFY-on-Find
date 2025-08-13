package com.sonfind.chelsea.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.dto.student.response.StudentListQueryDto;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	Optional<Student> findByStudentId(Long studentId);

	List<Student> findAllByTeamId(Long teamId);

	@Query("SELECT new com.sonfind.chelsea.dto.student.response.StudentListQueryDto(" +
		"s.studentId, s.name, s.majorYn, " +
		"pos.subCode, pos.subCodeName, " +
		"track.subCode, track.subCodeName, " +
		"goal.subCode, goal.subCodeName, " +
		"si.profile.profileImageUrl, " +
		"t.name, sf.isFavorite) " +
		"FROM Student s " +
		"LEFT JOIN StudentInfo si ON si.student = s " +
		"LEFT JOIN Team t ON t.teamId = s.teamId " +
		"LEFT JOIN si.positionCode pos " +
		"LEFT JOIN si.trackCode track " +
		"LEFT JOIN si.goalCode goal " +
		"LEFT JOIN StudentFavorite sf ON s.studentId = sf.targetStudent.studentId AND sf.student.studentId = :studentId "
		+
		"ORDER BY sf.isFavorite DESC, s.teamId ASC ,BINARY(s.name)"
	)
	List<StudentListQueryDto> findStudentList(@Param("studentId") Long studentId);

	@Query(value =
		"SELECT CASE WHEN s.major_yn = TRUE THEN '전공' ELSE '비전공' END AS major_type, "
			+ "COUNT(s.student_id) AS total_count, "
			+ "COUNT(s.team_id) AS teamMemberCount "
			+ "FROM students s "
			+ "LEFT JOIN teams t ON s.team_id = t.team_id "
			+ "GROUP BY major_type", nativeQuery = true)
	List<Object[]> getTeamRatio();

	@Query("select distinct s.studentId from Student s where s.teamId = :teamId")
	List<Long> findStudentIdsByTeamId(@Param("teamId") Long teamId);

	List<Student> findAllByTeamIdIn(List<Long> teamIds);

}
