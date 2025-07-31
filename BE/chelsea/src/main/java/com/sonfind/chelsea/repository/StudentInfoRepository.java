package com.sonfind.chelsea.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.studentInfo.StudentInfo;

@Repository
public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long> {
	// 학생 ID로 학생 정보를 조회하는 메소드
	StudentInfo findByStudentId(long studentId);
}
