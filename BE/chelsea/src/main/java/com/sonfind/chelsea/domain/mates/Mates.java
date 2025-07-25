package com.sonfind.chelsea.domain.mates;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sonfind.chelsea.domain.auth.Auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Getter
public class Mates {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "mate_id", referencedColumnName = "mate_id")
	Auth auth;

	private String techStack;

	private String strength;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	private String description;

	@Column(name = "portfolio_url")
	private String portfolioUrl;

	@Enumerated(EnumType.STRING)
	private Track track;

	@Enumerated(EnumType.STRING)
	private Position position;

	@Enumerated(EnumType.STRING)
	private Goal goal;

	@Enumerated(EnumType.STRING)
	private Mbti mbti;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;
}
