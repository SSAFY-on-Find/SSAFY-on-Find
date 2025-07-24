package com.sonfind.chelsea.service;

import com.sonfind.chelsea.domain.teams.Position;
import com.sonfind.chelsea.domain.teams.Recruitment;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.domain.teams.Track;
import com.sonfind.chelsea.dto.teams.CreateTeamRequest;
import com.sonfind.chelsea.repository.RecruitmentRepository;
import com.sonfind.chelsea.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

	private final TeamRepository teamRepository;
	private final RecruitmentRepository recruitmentRepository;

	//mate 관련 코드 모두 주석 처리
	@Transactional
	public Long createTeam(CreateTeamRequest request) {
		// Mate mate = findMateBySessionId(sessionId);
		//
		// if(mate.getTeam() != null){
		// 	throw new IllegalStateException("이미 팀에 소속된 사용자는 팀을 생성할 수 없습니다.");
		// }

		Track track = Track.valueOf(request.getTrack().toUpperCase());

		Team team = Team.builder()
			.name(request.getTeamName())
			.description(request.getDescription())
			.track(Track.valueOf(request.getTrack().toUpperCase())) // enum 변환
			.build();

		List<Recruitment> recruitments = request.getWishPositions().stream()
			.map(wish -> Recruitment.builder()
				.position(Position.valueOf(wish.getPosition().toUpperCase())) // enum 변환
				.remainingCount(wish.getCount())
				.team(team)
				.build())
			.collect(Collectors.toList());

		team.setRecruitments(recruitments);
		Team savedTeam = teamRepository.save(team);

		// mate.setTeam(team);
		// mateRepository.save(mate);

		return savedTeam.getId();
	}

	// mate 만들어지면 이 부분 바꿔야 함
	//임시로 만들었음
	// private Mate findMateBySessionId(String sessionId) {
	// 	//sessionId로 Mate 찾음
	// 	return mateRepository.findBySessionId(sessionId)
	// 		.orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
	// }


}
