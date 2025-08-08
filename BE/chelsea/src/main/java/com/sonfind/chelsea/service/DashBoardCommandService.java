package com.sonfind.chelsea.service;

import com.sonfind.chelsea.dto.dashboard.TeamProgressDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DashBoardCommandService {

	private final DashBoardQueryService dashBoardQueryService;
	private final ApplicationEventPublisher eventPublisher;

	// 팀 빌딩 진행률 이벤트를 발행하는 메소드
	public void publishTeamBuildingProgressEvent() {
		TeamProgressDto currentTeamProgressStatus = dashBoardQueryService.computeForTeamProgress();
		log.info("팀 빌딩 진행률 이벤트 발행: {}", currentTeamProgressStatus);

		eventPublisher.publishEvent(currentTeamProgressStatus);
	}

	// 교육생 정보 생성/업데이트 시 이벤트를 발행하는 메소드
	public void publishStudentInfoUpdateEvent() {
		log.info("교육생 정보 업데이트 이벤트 발행");
		eventPublisher.publishEvent("StudentInfoUpdated");
	}
}
