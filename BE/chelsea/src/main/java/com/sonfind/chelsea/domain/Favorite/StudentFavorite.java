package com.sonfind.chelsea.domain.Favorite;

import static lombok.AccessLevel.*;

import com.sonfind.chelsea.domain.student.Students;
import com.sonfind.chelsea.global.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "StudentFavorites", uniqueConstraints = @UniqueConstraint(
	name = "UK_student_favorite_student_target",
	columnNames = {"student_id", "target_student_id"}
))
public class StudentFavorite extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "is_favorite")
	private Boolean isFavorite;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Students student;

	@ManyToOne
	@JoinColumn(name = "target_student_id")
	private Students targetStudent;

}
