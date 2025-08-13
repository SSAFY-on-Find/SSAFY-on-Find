package com.sonfind.chelsea.service;

import com.sonfind.chelsea.dto.dashboard.*;
import com.sonfind.chelsea.types.MemberChageAction;
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

	// 팀원 변경 이벤트를 발행하는 메소드
	public void publishTeamMemberChangedEvent(Long teamId, MemberChageAction action, MemberSummary summary) {
		log.info("팀원 변경 이벤트 발행");
		TeamMemberChangedDto memberChangedDto = TeamMemberChangedDto.builder()
				.teamId(teamId)
				.action(action)
				.member(summary)
				.build();
		// 팀 목록 혹은 팀 상세보기 갱신용 이벤트 발행
		eventPublisher.publishEvent(memberChangedDto);
	}

	// 팀 정보 변경에 따른 이벤트를 발행하는 메소드
	public void publishTeamInfoUpdateEvent(TeamInfoUpdateDto eventDto) {
		log.info("팀 정보 업데이트 이벤트 발행");
		eventPublisher.publishEvent(eventDto);

	}

	// 교육생 정보 생성/업데이트 시 이벤트를 발행하는 메소드
	public void publishStudentInfoUpdateEvent(PositionChangeRequestDto eventDto) {
		log.info("교육생 정보 업데이트 이벤트 발행");
		eventPublisher.publishEvent(eventDto);
	}
}
