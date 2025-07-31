package com.sonfind.chelsea.domain.studentInfo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UploadedFile {

	private String savedFileName;
	private String originalFileName;

}
