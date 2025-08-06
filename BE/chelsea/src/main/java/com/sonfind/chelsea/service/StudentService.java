package com.sonfind.chelsea.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.dto.student.StudentListQueryDto;
import com.sonfind.chelsea.dto.student.StudentListResponseDto;
import com.sonfind.chelsea.dto.student.StudentResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.repository.StudentInfoRepository;
import com.sonfind.chelsea.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

	private final StudentRepository studentRepository;
	private final StudentInfoRepository studentInfoRepository;

	public Students findByStudentId(long studentId) {

		Optional<Students> authOptional = studentRepository.findByStudentId(studentId);
		Students student = null;

		if (authOptional.isPresent()) {
			student = authOptional.get();
		}

		return student;
	}

	public StudentResponseDto findByStudentIdForSse(long studentId) {
		Optional<Students> authOptional = studentRepository.findByStudentId(studentId);

		if (!authOptional.isPresent()) {
			throw new IllegalArgumentException("존재하지 않는 학생입니다.");
		}
		Students student = authOptional.get();

		return StudentResponseDto.builder()
			.studentId(student.getStudentId())
			.name(student.getName())
			.major(getIsMajor(student.getMajorYn()))
			.build();
	}

	//교육생 목록 조회
	@Transactional(readOnly = true)
	public List<StudentListResponseDto> getStudentList() {

		List<StudentListQueryDto> queryResult = studentRepository.findStudentList();

		return queryResult.stream().map(dto -> new StudentListResponseDto(
			new StudentResponseDto(dto.studentId(), dto.name(), dto.major() ? "전공" : "비전공"),
			new SubCodeResponseDto(dto.positionCode(), dto.positionCodeName()),
			new SubCodeResponseDto(dto.trackCode(), dto.trackCodeName()),
			new SubCodeResponseDto(dto.goalCode(), dto.goalCodeName()),
			dto.profileImageUrl(),
			false,
			dto.teamName()
		)).collect(Collectors.toList());
	}

	public StudentResponseDto createStudentResponse(Long studentId, String name, Boolean isMajor) {
		return new StudentResponseDto(
			studentId, name, getIsMajor(isMajor)
		);
	}

	private static String getIsMajor(Boolean major) {
		return major ? "전공" : "비전공";
	}

	public List<Students> findAllByTeamId(Long teamId) {
		return studentRepository.findAllByTeamId(teamId);
	}
}
