package com.sonfind.chelsea.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.studentInfo.StudentInfo;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoGetQueryDto;

@Repository
public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long> {

	boolean existsByStudent_StudentId(Long studentId);

	@EntityGraph(attributePaths = "student.classCode")
	Optional<StudentInfo> findByStudent_StudentId(Long studentId);

	@Query("SELECT new com.sonfind.chelsea.dto.studentInfo.StudentInfoGetQueryDto("
		+ " s.studentId, s.name, s.majorYn, "
		+ " pos.subCode, pos.subCodeName, "
		+ "track.subCode, track.subCodeName, "
		+ "goal.subCode, goal.subCodeName, "
		+ "mbti.subCode, mbti.subCodeName, "
		+ "si.techStack, si.strength, si.description, "
		+ "si.profileImageUrl, si.portfolio, "
		+ "t.teamId,t.name, "
		+ "COALESCE(t.majorCount,0), COALESCE(t.nonMajorCount,0) , "
		+ "teamTrack.subCodeName )"
		+ "FROM StudentInfo si "
		+ "JOIN si.student s "
		+ "LEFT JOIN Team t ON t.teamId = s.teamId "
		+ "LEFT JOIN t.track teamTrack "
		+ "LEFT JOIN si.positionCode pos "
		+ "LEFT JOIN si.trackCode track "
		+ "LEFT JOIN si.goalCode goal "
		+ "LEFT JOIN si.mbtiCode mbti "
		+ "WHERE si.student.studentId = :studentId")
	StudentInfoGetQueryDto findStudentInfoByStudentId(@Param("studentId") Long studentId);

}
