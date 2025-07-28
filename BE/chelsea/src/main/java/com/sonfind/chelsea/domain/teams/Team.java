package com.sonfind.chelsea.domain.teams;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "team_id")
	private Long teamId;

	//팀명, 팀 설명, 팀 트랙 유효성 추가
	//팀 명 자동 생성
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Track track;

	@Column(name = "is_deleted")
	private boolean isDeleted = false;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;

	//orphanRemoval 제거
	//List 초기화: NPE 방지 & 하이버네이트 호환성
	@OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
	@Builder.Default
	private List<Recruitment> recruitments = new ArrayList<>();

	public void updateDescription(String description) {
		this.description = description;
	}

	public void updateTrack(Track track) {
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

