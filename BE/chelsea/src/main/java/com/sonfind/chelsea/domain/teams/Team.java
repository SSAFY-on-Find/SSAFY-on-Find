package com.sonfind.chelsea.domain.teams;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

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

	@Column(nullable = false, length = 80)
	private String description;

	@ManyToOne
	@JoinColumn(name = "track_code", nullable = false)
	private SubCode track;

	@Column(name = "is_deleted")
	@ColumnDefault("false")
	private boolean isDeleted;

	@Column(name = "major_count", nullable = false)
	@ColumnDefault("0")
	// @Builder.Default
	private int majorCount = 0;

	@Column(name = "non_major_count", nullable = false)
	@ColumnDefault("0")
	// @Builder.Default
	private int nonMajorCount = 0;

	@OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
	@Builder.Default
	private List<Recruitment> recruitments = new ArrayList<>();

	public List<Recruitment> getRecruitments() {
		if (this.recruitments == null) {
			this.recruitments = new ArrayList<>();
		}
		return this.recruitments;
	}

	public void softDelete() {
		this.isDeleted = true;
	}

	public void updateDescription(String description) {
		this.description = description;
	}

	public void updateTrack(SubCode track) {
		this.track = track;
	}

	public void updatePositions(List<Recruitment> newRecruitments) {
		if (this.recruitments == null) {
			this.recruitments = new ArrayList<>();
		}
		this.recruitments.clear();
		this.recruitments.addAll(newRecruitments);
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void incrementMajorCount() {
		this.majorCount++;
	}

	public void decrementMajorCount() {
		this.majorCount = Math.max(0, this.majorCount - 1);
	}

	public void incrementNonMajorCount() {
		this.nonMajorCount++;
	}

	public void decrementNonMajorCount() {
		this.nonMajorCount = Math.max(0, this.nonMajorCount - 1);
	}
}
