package com.sonfind.chelsea.domain.Favorite;

import static lombok.AccessLevel.*;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.global.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Builder
@Table(name = "TeamFavorites")
public class TeamFavorite extends BaseEntity {

	@EmbeddedId
	private TeamFavoriteId id;

	@Column(name = "is_favorite")
	private Boolean isFavorite;

	@ManyToOne
	@MapsId("studentId")
	@JoinColumn(name = "student_id")
	private Students student;

	@ManyToOne
	@MapsId("teamId")
	@JoinColumn(name = "team_id")
	private Team team;

}
