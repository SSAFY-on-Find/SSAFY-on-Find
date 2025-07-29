package com.sonfind.chelsea.domain.mates;

import static lombok.AccessLevel.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.global.domain.BaseEntity;
import com.sonfind.chelsea.global.domain.SubCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@Getter
public class Mates extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "student_id", referencedColumnName = "student_id")
	Student student;

	private String techStack;

	private String strength;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	private String description;

	@Column(name = "portfolio_url")
	private String portfolioUrl;

	@ManyToOne
	@JoinColumn(name = "track_code")
	private SubCode track;

	@ManyToOne
	@JoinColumn(name = "position_code")
	private SubCode position;

	@ManyToOne
	@JoinColumn(name = "goal_code")
	private SubCode goal;

	@ManyToOne
	@JoinColumn(name = "mbti_code")
	private SubCode mbti;
}
