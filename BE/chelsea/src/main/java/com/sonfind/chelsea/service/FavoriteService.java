package com.sonfind.chelsea.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.sonfind.chelsea.domain.Favorite.TeamFavorite;
import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.dto.Favorite.StudentFavoriteResponseDto;
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
		Student student = studentRepository.findByStudentId(studentId)
			.orElseThrow(() -> new IllegalArgumentException("학생 없음"));

		//팀 있냐
		Team team = teamRepository.findTeamByTeamId(teamId)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "존재하지 않는 팀입니다."));

		if (team.isDeleted()) {
			throw new IllegalArgumentException("삭제된 팀입니다.");
		}

		//기존 좋아요 상태 확인
		Optional<TeamFavorite> existingFavorite = teamFavoriteRepository.findByStudentStudentIdAndTeamTeamId(studentId,
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
			TeamFavorite newFavorite = TeamFavorite.builder()
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

	//교육생 좋아요
	public StudentFavoriteResponseDto toggleFavoriteStudent(Long studentId, Long targetStudentId) {
		//본인 좋아요 안됨
		if(studentId.equals(targetStudentId)) {
			throw new IllegalArgumentException("본인 좋아요는 안돼요.");
		}

		//학생 있음?
		Student student = studentRepository.
	}

	//좋아요 상태 확인
	@Transactional(readOnly = true)
	public Boolean checkFavoriteStatus(Long studentId, Long teamId) {
		return teamFavoriteRepository.findByStudentStudentIdAndTeamTeamId(studentId, teamId)
			.map(TeamFavorite::getIsFavorite)
			.orElse(false);
	}

	public Map<Long, Boolean> checkFavoriteStatusBatch(Long studentId, List<Long> teamIds) {
		if (teamIds.isEmpty()) {
			return new HashMap<>();
		}

		// 한 번의 쿼리로 즐겨찾기된 팀들 조회 (isFavorite = true인 것만)
		List<Long> favoriteTeamIds = teamFavoriteRepository.findFavoriteTeamIdsByStudentIdAndTeamIds(studentId,
			teamIds);

		// 결과 Map 생성
		return teamIds.stream()
			.collect(Collectors.toMap(
				teamId -> teamId,
				teamId -> favoriteTeamIds.contains(teamId)
			));
	}

}
