package com.sonfind.chelsea.repository.studentInfo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonfind.chelsea.domain.studentInfo.StudentInfo;

public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long>, StudentInfoRepositoryCustom {

	Optional<StudentInfo> findByStudent_StudentId(Long studentId);
}
