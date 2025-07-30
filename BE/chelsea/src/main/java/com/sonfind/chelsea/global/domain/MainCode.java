package com.sonfind.chelsea.global.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MainCode extends BaseEntity {

	@Id
	@Column(name = "MAIN_CODE")
	private String mainCode;

	@Column(name = "MAIN_CODE_NAME")
	private String mainCodeName;

	@Column(name = "MAIN_CODE_DESCRIPTION")
	private String mainCodeDescription;

	@Column(name = "USE_YN")
	private boolean useYn;
}
