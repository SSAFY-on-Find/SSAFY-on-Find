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
				sc_pos.sub_code_name AS position_name,
				CASE 
					WHEN s.team_id IS NULL THEN 'notTeam'
					ELSE 'isTeam'
				END AS team_type,
				COUNT(si.id) AS student_count
			FROM student_info si
			INNER JOIN student s ON si.student_id = s.student_id
			LEFT JOIN sub_code sc_pos ON si.position_code = sc_pos.sub_code
			LEFT JOIN team t ON s.team_id = t.team_id
			GROUP BY
					position_name, team_type
			ORDER BY 
					position_name DESC ,team_type, student_count;
		""", nativeQuery = true)
	List<Object[]> getPositionMajorRatio();
}
