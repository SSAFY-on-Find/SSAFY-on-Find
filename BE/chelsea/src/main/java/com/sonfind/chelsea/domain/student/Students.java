package com.sonfind.chelsea.domain.student;

import static lombok.AccessLevel.*;

import com.sonfind.chelsea.domain.teams.Team;
import com.sonfind.chelsea.global.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Getter
public class Students extends BaseEntity {
	@Id
	@Column(name = "student_id")
	private long studentId;

	private String name;

	@Column(name = "major_yn")
	private boolean majorYn;

	@ManyToOne
	@Setter
	@JoinColumn(name = "team_id", referencedColumnName = "team_id")
	private Team team;
}
