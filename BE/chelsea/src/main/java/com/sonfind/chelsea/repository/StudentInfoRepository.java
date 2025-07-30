package com.sonfind.chelsea.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sonfind.chelsea.domain.mates.StudentInfo;

public interface StudentInfoRepository extends JpaRepository<StudentInfo, Long> {
	List<StudentInfo> findAllByStudent_TeamId(Long teamId);
}
