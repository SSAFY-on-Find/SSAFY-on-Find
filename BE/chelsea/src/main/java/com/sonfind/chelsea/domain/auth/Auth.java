package com.sonfind.chelsea.domain.auth;

import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Getter
public class Auth {
	@Id
	@Column(name = "mate_id")
	private long mateId;

	private String name;

	@Enumerated(EnumType.STRING)
	private Major major;

	@Column(name = "team_id")
	private Long teamId;
}
