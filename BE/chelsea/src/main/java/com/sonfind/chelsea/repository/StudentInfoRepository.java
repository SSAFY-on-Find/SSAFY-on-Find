package com.sonfind.chelsea.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.domain.studentInfo.StudentInfo;

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

	@Query("""
		SELECT si FROM StudentInfo si 
		LEFT JOIN FETCH si.positionCode 
		WHERE si.student.studentId IN :studentIds
		""")
	List<StudentInfo> findByStudent_StudentIdIn(@Param("studentIds") List<Long> studentIds);

	@Query("""
		SELECT si 
		FROM StudentInfo si 
		LEFT JOIN FETCH si.positionCode 
		WHERE si.student.studentId IN :studentIds
		""")
	List<StudentInfo> findBasicInfoByStudentIds(@Param("studentIds") List<Long> studentIds);

	List<StudentInfo> findAllByStudentIn(List<Student> students);
	@Query(value = """
			SELECT 
				sc_track.sub_code_name AS track_name,
				sc_pos.sub_code_name AS position_name,
				CASE 
					WHEN s.major_yn = TRUE THEN '전공'
					ELSE '비전공'
				END AS major_type,
				COUNT(si.id) AS student_count
			FROM student_info si
			LEFT JOIN student s ON si.student_id = s.student_id
			LEFT JOIN sub_code sc_track ON si.track_code = sc_track.sub_code
			LEFT JOIN sub_code sc_pos ON si.position_code = sc_pos.sub_code
			GROUP BY
					track_name,position_name, major_type
			ORDER BY 
					track_name, position_name,major_type, student_count;
		""", nativeQuery = true)
	List<Object[]> getTrackPositionMajorRatio();
}
