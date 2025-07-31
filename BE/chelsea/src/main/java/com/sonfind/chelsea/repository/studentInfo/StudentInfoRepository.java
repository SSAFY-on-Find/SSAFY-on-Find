package com.sonfind.chelsea.repository.studentInfo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonfind.chelsea.domain.studentInfo.StudentInfo;

public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long>, StudentInfoRepositoryCustom {

	//StudentInfoResponseDto findStudentInfoByStudentId(Long studentId);
	Optional<StudentInfo> findByStudent_StudentId(Long studentId);
}
