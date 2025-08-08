package com.sonfind.chelsea.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.Favorite.TeamFavorite;

@Repository
public interface TeamFavoriteRepository extends JpaRepository<TeamFavorite, Long> {
	Optional<TeamFavorite> findByStudentStudentIdAndTeamTeamId(Long studentId, Long teamId);

}

