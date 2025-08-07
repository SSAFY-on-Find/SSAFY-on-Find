package com.sonfind.chelsea.global.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubCode extends BaseEntity {

	@Id
	@Column(name = "SUB_CODE")
	private String subCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MAIN_CODE")
	private MainCode mainCode;

	@Column(name = "SUB_CODE_NAME")
	private String subCodeName;

	@Column(name = "SUB_CODE_DESCRIPTION")
	private String subCodeDescription;

	@Column(name = "USE_YN")
	private boolean useYn;
}
