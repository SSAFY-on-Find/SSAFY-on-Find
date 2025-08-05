package com.sonfind.chelsea.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sonfind.chelsea.domain.Favorite.TeamFavorite;
import com.sonfind.chelsea.domain.Favorite.TeamFavoriteId;
import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.Favorite.TeamFavoriteResponseDto;
import com.sonfind.chelsea.repository.StudentRepository;
import com.sonfind.chelsea.repository.TeamFavoriteRepository;
import com.sonfind.chelsea.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
	private final TeamRepository teamRepository;
	private final StudentRepository studentRepository;
	private final TeamFavoriteRepository teamFavoriteRepository;

	//팀 좋아요(추가/제거)
	public TeamFavoriteResponseDto toggleFavoriteTeam(Long studentId, Long teamId) {
		//학생 있냐
		Students student = studentRepository.findByStudentId(studentId)
			.orElseThrow(() -> new IllegalArgumentException("학생 없음"));

		//팀 있냐
		Team team = teamRepository.findTeamByTeamId(teamId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "존재하지 않는 팀입니다."));

		if (team.isDeleted()) {
			throw new IllegalArgumentException("삭제된 팀입니다.");
		}

		//기존 좋아요 상태 확인
		Optional<TeamFavorite> existingFavorite = teamFavoriteRepository.findByIdStudentIdAndIdTeamId(studentId,
			teamId);
		//좋취
		if (existingFavorite.isPresent()) {
			teamFavoriteRepository.delete(existingFavorite.get());

			return TeamFavoriteResponseDto.builder()
				.isFavorite(false)
				.build();
		}
		//좋아요
		else {
			TeamFavoriteId teamFavoriteId = new TeamFavoriteId(studentId, teamId);
			TeamFavorite newFavorite = TeamFavorite.builder()
				.id(teamFavoriteId)
				.isFavorite(true)
				.student(student)
				.team(team)
				.build();

			teamFavoriteRepository.save(newFavorite);

			return TeamFavoriteResponseDto.builder()
				.isFavorite(true)
				.build();
		}
	}

	//좋아요 상태 확인
	@Transactional(readOnly = true)
	public Boolean checkFavoriteStatus(Long studentId, Long teamId) {
		return teamFavoriteRepository.findByIdStudentIdAndIdTeamId(studentId, teamId)
			.map(TeamFavorite::getIsFavorite)
			.orElse(false);
	}

}
