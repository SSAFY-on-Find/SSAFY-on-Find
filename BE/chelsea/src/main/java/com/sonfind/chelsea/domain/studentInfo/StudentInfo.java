package com.sonfind.chelsea.domain.studentInfo;

import static lombok.AccessLevel.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.global.domain.BaseEntity;
import com.sonfind.chelsea.global.domain.SubCode;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import lombok.experimental.SuperBuilder;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
@SuperBuilder
@Getter
public class StudentInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "student_id", referencedColumnName = "student_id")
	Students student;

	private String techStack;

	private String strength;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	@Embedded
	@AttributeOverride(name = "savedFileName", column = @Column(name = "portfolio_saved_filename"))
	@AttributeOverride(name = "originalFileName", column = @Column(name = "portfolio_original_filename"))
	private UploadedFile portfolio;

	private String description;

	@ManyToOne
	@JoinColumn(name = "track_code")
	private SubCode trackCode;

	@ManyToOne
	@JoinColumn(name = "position_code")
	private SubCode positionCode;

	@ManyToOne
	@JoinColumn(name = "goal_code")
	private SubCode goalCode;

	@ManyToOne
	@JoinColumn(name = "mbti_code")
	private SubCode mbtiCode;
}
