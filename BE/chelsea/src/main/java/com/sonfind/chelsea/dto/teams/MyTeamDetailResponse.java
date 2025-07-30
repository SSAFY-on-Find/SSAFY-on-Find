package com.sonfind.chelsea.dto.teams;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyTeamDetailResponse extends TeamDetailResponse {
	private boolean rule001; //SIZE_LIMIT <= 6
	private boolean rule002; //MIN_MAJOR >= 2
	private boolean rule003; //MIN_NON_MAJOR >= 2
	//대기 요청 목록
	//팀 채팅

	public MyTeamDetailResponse(
		TeamDetailResponse base,
		boolean rule001,
		boolean rule002,
		boolean rule003
		//대기 요청 목록
		//팀 채팅
	) {
		super(
			base.getTeamId(),
			base.getTeamName(),
			base.getDescription(),
			base.getTrackCode(),
			base.getTrackName(),
			base.getRecruitments(),
			base.getMemberCount(),
			base.getMembers()
		);
		this.rule001 = rule001;
		this.rule002 = rule002;
		this.rule003 = rule003;
		//대기 요청 목록
		//팀 채팅
	}

}
