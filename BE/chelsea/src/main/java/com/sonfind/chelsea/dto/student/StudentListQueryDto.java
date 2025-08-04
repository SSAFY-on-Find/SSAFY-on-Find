package com.sonfind.chelsea.dto.student;

public record StudentListQueryDto(
	Long studentId,
	String name,
	Boolean major,
	String positionCode,
	String positionCodeName,
	String trackCode,
	String trackCodeName,
	String goalCode,
	String goalCodeName,
	String profileImageUrl,
	String teamName
) {

}
