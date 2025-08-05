package com.sonfind.chelsea.repository.studentInfo;

import static com.sonfind.chelsea.domain.student.QStudents.*;
import static com.sonfind.chelsea.domain.studentInfo.QStudentInfo.*;
import static com.sonfind.chelsea.domain.teams.QTeam.*;

import java.util.Collections;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sonfind.chelsea.domain.student.QStudents;
import com.sonfind.chelsea.domain.studentInfo.UploadedFile;
import com.sonfind.chelsea.dto.student.StudentResponse;
import com.sonfind.chelsea.dto.studentInfo.StudentInfoResponseDto;
import com.sonfind.chelsea.dto.subcode.SubCodeResponse;
import com.sonfind.chelsea.dto.teams.TeamResponse;
import com.sonfind.chelsea.global.domain.QSubCode;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StudentInfoRepositoryCustomImpl implements StudentInfoRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<StudentInfoResponseDto> findStudentInfoResponseDtoById(Long studentInfoId) {

		QSubCode position = new QSubCode("position");
		QSubCode track = new QSubCode("track");
		QSubCode goal = new QSubCode("goal");
		QSubCode mbti = new QSubCode("mbti");
		QSubCode teamTrack = new QSubCode("teamTrack");

		//팀 인원 수 계산
		QStudents subStudents = new QStudents("subStudents");

		StudentInfoResponseDto result = queryFactory
			.select(Projections.constructor(StudentInfoResponseDto.class,
				// 학생 정보 get
				Projections.constructor(StudentResponse.class,
					students.studentId,
					students.name,
					new CaseBuilder()
						.when(students.majorYn.isTrue()).then("전공")
						.otherwise("비전공")
				),
				// position
				Projections.constructor(SubCodeResponse.class, position.subCode, position.subCodeName),
				// track
				Projections.constructor(SubCodeResponse.class, track.subCode, track.subCodeName),
				// goal
				Projections.constructor(SubCodeResponse.class, goal.subCode, goal.subCodeName),
				// mbti
				Projections.constructor(SubCodeResponse.class, mbti.subCode, mbti.subCodeName),
				//techstack empty list
				Expressions.constant(Collections.emptyList()),
				//strength empty list
				Expressions.constant(Collections.emptyList()),
				studentInfo.description,
				studentInfo.profileImageUrl,
				//portfolio
				Projections.constructor(UploadedFile.class,
					studentInfo.portfolio.originalFileName,
					studentInfo.portfolio.savedFileName
				),
				//Team
				Projections.constructor(TeamResponse.class,
					team.name,
					Projections.constructor(SubCodeResponse.class, team.track.subCode, team.track.subCodeName),
					JPAExpressions
						.select(subStudents.count())
						.from(subStudents)
						.where(subStudents.teamId.eq(team.teamId))
				)
			))
			.from(studentInfo)
			.join(studentInfo.student, students)
			.leftJoin(team).on(students.teamId.eq(team.teamId))
			.leftJoin(studentInfo.positionCode, position)
			.leftJoin(studentInfo.trackCode, track)
			.leftJoin(studentInfo.goalCode, goal)
			.leftJoin(studentInfo.mbtiCode, mbti)
			.leftJoin(team.track, teamTrack)
			.where(studentInfo.student.studentId.eq(studentInfoId))
			.fetchOne();

		return Optional.ofNullable(result);
	}
}
