package com.sonfind.chelsea.repository.studentInfo;

import java.util.Optional;

import com.sonfind.chelsea.dto.studentInfo.StudentInfoResponseDto;

public interface StudentInfoRepositoryCustom {
	Optional<StudentInfoResponseDto> findStudentInfoResponseDtoById(Long studentInfoId);
}
