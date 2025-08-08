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
