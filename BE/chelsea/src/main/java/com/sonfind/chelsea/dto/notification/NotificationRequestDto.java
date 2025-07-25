package com.sonfind.chelsea.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationRequestDto {
	private Long subId;
	private String subType;
	private Long pubId;
	private String pubType;
}
