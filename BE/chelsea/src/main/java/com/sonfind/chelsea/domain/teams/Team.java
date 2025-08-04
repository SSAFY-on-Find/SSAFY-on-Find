package com.sonfind.chelsea.domain.teams;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import com.sonfind.chelsea.global.domain.BaseEntity;
import com.sonfind.chelsea.global.domain.SubCode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "Teams")
public class Team extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "team_id")
	private Long teamId;

	@Column(nullable = false)
	private String name;

	//varchar(80)
	@Column(nullable = false)
	private String description;

	@ManyToOne
	@JoinColumn(name = "track_code", nullable = false)
	private SubCode track;

	@Column(name = "is_deleted")
	private boolean isDeleted = false;

	@OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
	@Builder.Default
	private List<Recruitment> recruitments = new ArrayList<>();

	public void updateDescription(String description) {
		this.description = description;
	}

	public void updateTrack(SubCode track) {
		this.track = track;
	}

	public void updatePositions(List<Recruitment> newRecruitments) {
		this.recruitments.clear();
		this.recruitments.addAll(newRecruitments);
	}

	public void updateName(String name) {
		this.name = name;
	}
}

