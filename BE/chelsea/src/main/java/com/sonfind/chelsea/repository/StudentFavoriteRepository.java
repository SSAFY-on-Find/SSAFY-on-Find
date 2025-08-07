package com.sonfind.chelsea.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.Favorite.StudentFavorite;

@Repository
public interface StudentFavoriteRepository extends JpaRepository<StudentFavorite, Long> {
	Optional<StudentFavorite> findByStudentStudentIdAndTargetStudentStudentId(Long studentId, Long targetStudentId);

	@Query("SELECT sf.targetStudent.studentId FROM StudentFavorite sf " +
		"WHERE sf.student.studentId = :studentId " +
		"AND sf.targetStudent.studentId IN :targetStudentIds " +
		"AND sf.isFavorite = true")
	List<Long> findFavoriteStudentIdsByStudentIdAndTargetStudentIds(
		@Param("studentId") Long studentId,
		@Param("targetStudentIds") List<Long> targetStudentIds);

}
