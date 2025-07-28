package com.sonfind.chelsea.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sonfind.chelsea.domain.teams.Recruitment;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.teams.CreateTeamRequest;
import com.sonfind.chelsea.dto.teams.UpdateTeamRequest;
import com.sonfind.chelsea.dto.teams.WishPosition;
import com.sonfind.chelsea.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;

	//공통: Mate 로직 추가해야 함

	//팀 생성
	//팀 명 자동생성 추가
	@Transactional
	public Long createTeam(CreateTeamRequest request) {

		//팀 명 없이 일단 저장
		Team noTeamName = Team.builder()
			.name("")
			.description(request.getDescription())
			.track(request.getTrack())
			.build();

		Team team = teamRepository.save(noTeamName);

		//자동 팀명 생성(e.g. 001-팀)
		String autoName = String.format("%03d-팀", team.getTeamId());
		team.updateName(autoName);

		// Recruitment 리스트 변환
		List<Recruitment> recruitments = toRecruitments(request.getWishPositions(), team);
		team.updatePositions(recruitments);

		return team.getTeamId();
	}

	//팀 수정
	@Transactional
	public void updateTeamInfo(Long teamId, String sessionId, UpdateTeamRequest request) {
		Team team = teamRepository.findById(teamId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));

		if (request.getDescription() != null) {
			team.updateDescription(request.getDescription());
		}

		if (request.getTrack() != null) {
			team.updateTrack(request.getTrack());
		}

		if (request.getWishPositions() != null) {
			List<Recruitment> recruitments = toRecruitments(request.getWishPositions(), team);
			team.updatePositions(recruitments);
		}
	}

	//wishPositionDto를 recruitment entity로 변환
	private List<Recruitment> toRecruitments(List<WishPosition> wishes, Team team) {
		return wishes.stream()
			.map(w -> Recruitment.builder()
				.position(w.getPosition())
				.remainingCount(w.getCount())
				.team(team)
				.build())
			.collect(Collectors.toList());
	}

}
