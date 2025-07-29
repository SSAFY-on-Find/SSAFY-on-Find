package com.sonfind.chelsea.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.student.Students;

@Repository
public interface StudentRepository extends JpaRepository<Students, Long> {
	Optional<Students> findByStudentId(Long studentId); // 원시형 vs wrapper 형?
}
