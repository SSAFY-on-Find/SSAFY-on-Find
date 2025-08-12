package com.sonfind.chelsea.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.dto.dashboard.TeamRatioDto;
import com.sonfind.chelsea.dto.student.request.StudentSignInRequestDto;
import com.sonfind.chelsea.dto.student.response.StudentListQueryDto;
import com.sonfind.chelsea.dto.student.response.StudentListResponseDto;
import com.sonfind.chelsea.dto.student.response.StudentResponseDto;
import com.sonfind.chelsea.dto.student.response.StudentSignInResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponseDto;
import com.sonfind.chelsea.global.error.AppException;
import com.sonfind.chelsea.repository.StudentInfoRepository;
import com.sonfind.chelsea.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

	private final StudentRepository studentRepository;
	private final StudentInfoRepository studentInfoRepository;

	/**
	 * 로그인 처리 service
	 * */
	public StudentSignInResponseDto signIn(StudentSignInRequestDto request) {

		Long studentId = Long.parseLong(request.studentId());

		return getLoginCheck(studentId);
	}

	/**
	 * 전체 교육생 목록 조회
	 * */
	@Transactional(readOnly = true)
	public List<StudentListResponseDto> getStudentList(Long studentId) {

		List<StudentListQueryDto> queryResult = studentRepository.findStudentList(studentId);

		return queryResult.stream().map(dto -> new StudentListResponseDto(
			new StudentResponseDto(dto.studentId(), dto.name(), dto.major() ? "전공" : "비전공"),
			new SubCodeResponseDto(dto.positionCode(), dto.positionCodeName()),
			new SubCodeResponseDto(dto.trackCode(), dto.trackCodeName()),
			new SubCodeResponseDto(dto.goalCode(), dto.goalCodeName()),
			dto.profileImageUrl(),
			dto.isFavorite() != null,
			dto.teamName()
		)).collect(Collectors.toList());
	}

	public Student findByStudentId(long studentId) {

		Optional<Student> authOptional = studentRepository.findByStudentId(studentId);

		if (authOptional.isEmpty()) {
			throw AppException.studentNotFound();
		}

		return authOptional.get();
	}

	public StudentResponseDto findByStudentIdForSse(long studentId) {
		Optional<Student> authOptional = studentRepository.findByStudentId(studentId);

		if (authOptional.isEmpty()) {
			throw AppException.studentNotFound();
		}
		Student student = authOptional.get();

		return StudentResponseDto.builder()
			.studentId(student.getStudentId())
			.name(student.getName())
			.major(getIsMajor(student.getMajorYn()))
			.build();
	}

	/**
	 * 대시보드 용 팀 빌딩 진행률 조회 함수
	 * */
	@Transactional(readOnly = true)
	public Map<String, TeamRatioDto> getTeamRatio() {

		List<Object[]> rawData = studentRepository.getTeamRatio();

		Map<String, TeamRatioDto> result = new HashMap<>();
		int totalStudentCount = 0;
		int totalTeamMemberCount = 0;

		for (Object[] row : rawData) {
			String majorType = row[0].toString();
			Long longTotalCount = (Long)row[1];
			Long longTeamMemberCount = (Long)row[2];
			int totalCount = longTotalCount.intValue();
			int teamMemberCount = longTeamMemberCount.intValue();

			totalStudentCount += totalCount;
			totalTeamMemberCount += teamMemberCount;

			result.put(majorType.equals("전공") ? "major" : "nonMajor", TeamRatioDto.builder()
				.type(majorType)
				.totalStudentCount(totalCount)
				.teamMemberCount(teamMemberCount).build());
		}
		result.put("all", TeamRatioDto.builder()
			.type("전체")
			.totalStudentCount(totalStudentCount)
			.teamMemberCount(totalTeamMemberCount).build());

		return result;
	}

	public StudentSignInResponseDto getLoginCheck(long studentId) {

		Student student = studentRepository.findByStudentId(studentId)
			.orElseThrow(AppException::studentNotFound);

		String className = student.getClassCode().getSubCodeName();

		boolean isCreatedStudentInfo = studentInfoRepository.existsByStudent_StudentId(studentId);

		return StudentSignInResponseDto.builder()
			.studentId(student.getStudentId())
			.name(student.getName())
			.major(getIsMajor(student.getMajorYn()))
			.className(className)
			.teamId(student.getTeamId())
			.isCreatedStudentInfo(isCreatedStudentInfo)
			.build();
	}

  public List<Long> getStudentIdList(Long teamId) {
    return studentRepository.findIdsByTeamId(teamId);
  }

	private static String getIsMajor(Boolean major) {
		return Boolean.TRUE.equals(major) ? "전공" : "비전공";
	}

	public List<Student> findAllByTeamId(Long teamId) {
		return studentRepository.findAllByTeamId(teamId);
	}
}
