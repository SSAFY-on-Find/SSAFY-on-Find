package com.sonfind.chelsea.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sonfind.chelsea.domain.Favorite.TeamFavorite;
import com.sonfind.chelsea.domain.Favorite.TeamFavoriteId;

@Repository
public interface TeamFavoriteRepository extends JpaRepository<TeamFavorite, TeamFavoriteId> {
	Optional<TeamFavorite> findByIdStudentIdAndIdTeamId(Long studentId, Long teamId);

}
