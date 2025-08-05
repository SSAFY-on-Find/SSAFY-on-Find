package com.sonfind.chelsea.domain.Favorite;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TeamFavoriteId implements Serializable {
	@Column(name = "student_id")
	private Long studentId;

	@Column(name = "team_id")
	private Long teamId;
}
