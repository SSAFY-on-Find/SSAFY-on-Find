package com.sonfind.chelsea.service;

import com.sonfind.chelsea.dto.dashboard.TeamProgressDto;
import com.sonfind.chelsea.dto.dashboard.TeamRatioDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashBoardQueryService {

	private final StudentService studentService;

	/**
	 * 희망 트랙별 통계 정보를 조회합니다.
	 */
//	public Map<String, TrackPositionMajorRatioResponseDto> computeForTrackPositionMajorRatio() {
//		log.info("희망 트랙별 통계 정보를 조회합니다.");
////		return studentService.getTrackPositionMajorRatio();
//	}


	/**
	 * 전체 팀빌딩 진행률을 계산합니다.
	 * 팀에 속한 인원 수와 전공 여부를 기반으로 진행률을 계산합니다.
	 *
	 * @return TeamProgressDto 객체로 팀빌딩 진행률, 비전공자 수, 전공자 수를 포함합니다.
	 */
	public TeamProgressDto computeForTeamProgress() {
		log.info("전체 팀빌딩 진행률을 계산합니다.");
		Map<String, TeamRatioDto> teamRatio = studentService.getTeamRatio();

		String nonMajor = "비전공";
		String major = "전공";

		int totalStudentCount = teamRatio.values().stream()
				.mapToInt(TeamRatioDto::totalStudentCount)
				.sum();

		int progressRate = (teamRatio.get(major).teamMemberCount() +
				teamRatio.get(nonMajor).teamMemberCount()) * 100 / totalStudentCount;

		return TeamProgressDto.builder()
				.progressRate(progressRate)
				.totalNonMajorCount(teamRatio.get(nonMajor).totalStudentCount())
				.teamMemberNonMajorCount(teamRatio.get(nonMajor).teamMemberCount())
				.totalMajorCount(teamRatio.get(major).totalStudentCount())
				.teamMemberMajorCount(teamRatio.get(major).teamMemberCount())
				.build();
	}
}
