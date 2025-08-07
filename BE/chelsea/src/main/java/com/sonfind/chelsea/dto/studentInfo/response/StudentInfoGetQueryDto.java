package com.sonfind.chelsea.dto.studentInfo.response;

import com.sonfind.chelsea.domain.studentInfo.UploadedFile;

public record StudentInfoGetQueryDto(
	Long studentId,
	String name,
	Boolean isMajor,
	String positionCode,
	String positionCodeName,
	String trackCode,
	String trackCodeName,
	String goalCode,
	String goalCodeName,
	String mbtiCode,
	String mbtiCodeName,
	String techStackString,
	String strengthString,
	String description,
	String profileImageUrl,
	UploadedFile portfolio,
	Long teamId,
	String teamName,
	Integer majorCount,
	Integer nonMajorCount,
	String teamTrackCodeName
) {
}