package com.sonfind.chelsea.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.dto.student.response.StudentResponseDto;
import com.sonfind.chelsea.dto.student.response.StudentUnionForNotificationResponseDto;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoForNotificationResponseDto;
import com.sonfind.chelsea.service.StudentInfoService;
import com.sonfind.chelsea.service.StudentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentFacade {
	private final StudentService studentService;
	private final StudentInfoService studentInfoService;

	// 학생 ID로 학생 정보를 조회하는 메소드
	public Students findByStudentId(long studentId) {
		return studentService.findByStudentId(studentId);
	}

	// 팀 ID로 학생 정보 리스트를 조회하는 메소드
	public List<Students> findAllByTeamId(Long teamId) {
		return studentService.findAllByTeamId(teamId);
	}

	// 학생 ID로 학생 정보를 조회하는 메소드(SSE용)
	public StudentUnionForNotificationResponseDto findByStudentIdForSse(Long studentId) {
		StudentResponseDto findStudent = studentService.findByStudentIdForSse(studentId);
		StudentInfoForNotificationResponseDto findStudentInfo = studentInfoService.findByStudentId(
			findStudent.studentId());

		return StudentUnionForNotificationResponseDto.builder()
			.studentId(findStudent.studentId())
			.name(findStudent.name())
			.isMajor(findStudent.major())
			.position(findStudentInfo.position())
			.track(findStudentInfo.track())
			.profileImageUrl(findStudentInfo.profileImageUrl())
			.build();
	}

	// 해당 학생이 팀의 멤버인지 확인하는 메소드
	public Boolean isMemberOfTeam(Long StudentId, Long teamId) {
		if (teamId == null) {
			return false; // 팀 ID가 없으면 false 반환
		}
		List<Students> teamMembers = studentService.findAllByTeamId(teamId);
		for (Students member : teamMembers) {
			if (member.getStudentId() == StudentId) {
				return true; // 학생이 팀의 멤버인 경우 true 반환
			}
		}
		return false; // 학생이 팀의 멤버가 아닌 경우 false 반환
	}
}
