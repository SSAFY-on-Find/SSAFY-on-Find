package com.sonfind.chelsea.domain.student;

import static lombok.AccessLevel.*;

import com.sonfind.chelsea.global.domain.BaseEntity;
import com.sonfind.chelsea.global.domain.SubCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Getter
@Table(name = "Students")
public class Student extends BaseEntity {
	@Id
	@Column(name = "student_id")
	private long studentId;

	@Column(nullable = false)
	private String name;

	@Column(name = "major_yn", nullable = false)
	private Boolean majorYn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_code", nullable = false)
	private SubCode classCode;

	@Setter
	@JoinColumn(name = "team_id", referencedColumnName = "team_id")
	private Long teamId;
}
