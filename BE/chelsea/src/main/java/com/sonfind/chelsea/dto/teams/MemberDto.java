package com.sonfind.chelsea.dto.teams;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberDto {
	private Long studentId;
	private String name;
	private boolean majorYn;
	private String profileImageUrl;
	private String position;
}
