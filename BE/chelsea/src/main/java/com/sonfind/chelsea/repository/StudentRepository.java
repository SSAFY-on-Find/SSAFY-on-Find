package com.sonfind.chelsea.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.dto.student.response.StudentListQueryDto;

@Repository
public interface StudentRepository extends JpaRepository<Students, Long> {

	Optional<Students> findByStudentId(Long studentId);

	List<Students> findAllByTeamId(Long teamId);

	@Query("SELECT new com.sonfind.chelsea.dto.student.response.StudentListQueryDto(" +
		"s.studentId, s.name, s.majorYn, " +
		"pos.subCode, pos.subCodeName, " +
		"track.subCode, track.subCodeName, " +
		"goal.subCode, goal.subCodeName, " +
		"si.profileImageUrl, " +
		"t.name) " +
		"FROM StudentInfo si " +
		"RIGHT JOIN si.student s " +
		"LEFT JOIN Team t ON t.teamId = s.teamId " +
		"LEFT JOIN si.positionCode pos " +
		"LEFT JOIN si.trackCode track " +
		"LEFT JOIN si.goalCode goal")
	List<StudentListQueryDto> findStudentList();
}
