package com.sonfind.chelsea.service;

import com.sonfind.chelsea.dto.dashboard.TeamProgressDto;
import com.sonfind.chelsea.dto.dashboard.TeamRatioDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashBoardQueryService {

	private final StudentService studentService;

	/**
	 * 전체 팀빌딩 진행률을 계산합니다.
	 * 팀에 속한 인원 수와 전공 여부를 기반으로 진행률을 계산합니다.
	 *
	 * @return TeamProgressDto 객체로 팀빌딩 진행률, 비전공자 수, 전공자 수를 포함합니다.
	 */
	public TeamProgressDto computeForTeamProgress() {
		List<TeamRatioDto> teamRatio = studentService.getTeamRatio();

	}
}
