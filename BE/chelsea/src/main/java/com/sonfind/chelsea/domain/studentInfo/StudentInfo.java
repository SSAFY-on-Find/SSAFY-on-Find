package com.sonfind.chelsea.domain.studentInfo;

import static lombok.AccessLevel.*;

import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sonfind.chelsea.domain.student.Student;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoUpdateRequestDto;
import com.sonfind.chelsea.global.domain.BaseEntity;
import com.sonfind.chelsea.global.domain.SubCode;
import com.sonfind.chelsea.util.StringListConverter;

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
	Student student;

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

	public void update(StudentInfoUpdateRequestDto requestDto, List<SubCode> subCodes) {
		if (requestDto.techStack() != null && !requestDto.techStack().isEmpty()) {
			String techStackString = StringListConverter.listToString(requestDto.techStack());
			this.techStack = techStackString;
		}

		if (requestDto.strength() != null && !requestDto.strength().isEmpty()) {
			String strengthString = StringListConverter.listToString(requestDto.strength());
			this.strength = strengthString;
		}

		if (requestDto.description() != null && !requestDto.description().isEmpty()) {
			this.description = requestDto.description();
		}

		if (requestDto.track() != null && !requestDto.track().isEmpty()) {
			SubCode trackCode = getSubCodeBySubCode(subCodes, requestDto.track());
			this.trackCode = trackCode;
		}
		if (requestDto.position() != null && !requestDto.position().isEmpty()) {
			SubCode positionCode = getSubCodeBySubCode(subCodes, requestDto.position());
			this.positionCode = positionCode;
		}
		if (requestDto.goal() != null && !requestDto.goal().isEmpty()) {
			SubCode goalCode = getSubCodeBySubCode(subCodes, requestDto.goal());
			this.goalCode = goalCode;
		}
		if (requestDto.mbti() != null && !requestDto.mbti().isEmpty()) {
			SubCode mbtiCode = getSubCodeBySubCode(subCodes, requestDto.mbti());
			this.mbtiCode = mbtiCode;
		}
	}

	private SubCode getSubCodeBySubCode(List<SubCode> subCodes, String code) {
		if (code == null)
			return null; // 업데이트하지 않을 필드는 null로 들어올 수 있음
		return subCodes.stream()
			.filter(sc -> sc.getSubCode().equals(code))
			.findFirst()
			.orElse(null);
	}

	public void updateProfileImage(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public void updatePortfolio(UploadedFile portfolio) {
		this.portfolio = portfolio;
	}

}
