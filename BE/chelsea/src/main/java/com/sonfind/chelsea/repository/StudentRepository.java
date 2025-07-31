package com.sonfind.chelsea.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.student.Students;

@Repository
public interface StudentRepository extends JpaRepository<Students, Long> {
	Optional<Students> findByStudentId(Long studentId);

	List<Students> findAllByTeamId(Long teamId);
}
